package com.example.weatherapp.data.tasks;

import android.os.Build;
import android.util.Log;

import com.example.weatherapp.WeatherParsedListener;
import com.example.weatherapp.data.BasicWeather;
import com.example.weatherapp.data.ExtendedWeather;
import com.google.android.gms.maps.model.LatLng;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetWeatherTask implements Runnable
{
    public GetWeatherTask(WeatherParsedListener weatherParsedListener, String urlThree, String urlOne)
    {
        this.weatherParsedListener = weatherParsedListener;
        this.urlThree = urlThree;
        this.urlOne = urlOne;
    }
    private final WeatherParsedListener weatherParsedListener;
    private final String urlOne;
    private final String urlThree;
    private String oneDayResult;
    private String threeDayResult;
    private ArrayList<BasicWeather> threeDayWeather;
    private ArrayList<ExtendedWeather> threeDayWeatherExtended;
    private ExtendedWeather oneDayWeather;
    private ExtendedWeather OneDayWeatherExtended;
    private OneDayData oneDayData;
    private boolean isOneDayFinished;
    private final Object status = new Object();
    @Override
    public void run()
    {
        getOneDayXML();
        getThreeDayXML();
    }
    private void createDetailedOneDay()
    {
        if(oneDayWeather != null)
        {
            OneDayWeatherExtended = oneDayWeather;

            OneDayWeatherExtended.setUvRisk(oneDayData.getUvRisk());
            OneDayWeatherExtended.setHighTemperature(oneDayData.getMaxTemperature());
            OneDayWeatherExtended.setLowTemperature(oneDayData.getMinTemperature());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                OneDayWeatherExtended.setSunrise(oneDayData.getSunrise());
                OneDayWeatherExtended.setSunset(oneDayData.getSunset());
            }

            Log.d("1D", "oneDayData = Called");

        }
        else
        {
            Log.d("ERROR", "oneDayData = Null");
        }

    }


    private static class OneDayData
    {
        private final int uvRisk;
        private final LocalTime sunrise, sunset;
        private final float maxTemperature, minTemperature;

        public OneDayData(int uvRisk, LocalTime sunrise, LocalTime sunset, float maxTemperature, float minTemperature)
        {
            this.uvRisk = uvRisk;
            this.sunrise = sunrise;
            this.sunset = sunset;
            this.maxTemperature = maxTemperature;
            this.minTemperature = minTemperature;
        }

        public int getUvRisk() {
            return uvRisk;
        }

        public LocalTime getSunrise() {
            return sunrise;
        }
        public LocalTime getSunset() {
            return sunset;
        }


        public float getMaxTemperature() {
            return maxTemperature;
        }

        public float getMinTemperature() {
            return minTemperature;
        }
    }

    private void getOneDayXML()
    {

        isOneDayFinished = false;
        URL aurl;
        URLConnection _url;
        BufferedReader in = null;
        String inputLine = "";
        try
        {
            aurl = new URL(urlOne);
            _url = aurl.openConnection();
            in = new BufferedReader(new InputStreamReader((_url.getInputStream())));
            while((inputLine = in.readLine()) != null){
                oneDayResult = oneDayResult + inputLine;
                Log.d("1D TEST", inputLine);
            }
            in.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        int i = oneDayResult.indexOf(">");
        oneDayResult = oneDayResult.substring(i+1);

        parseOneDayXML();
    }
    private void getThreeDayXML()
    {

        URL aurl;
        URLConnection _url;
        BufferedReader in = null;
        String inputLine = "";

        try
        {
            aurl = new URL(urlThree);
            _url = aurl.openConnection();
            in = new BufferedReader(new InputStreamReader((_url.getInputStream())));
            while((inputLine = in.readLine()) != null){
                threeDayResult = threeDayResult + inputLine;
                Log.d("3D TEST", inputLine);
            }
            in.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        int i = threeDayResult.indexOf(">");
        threeDayResult = threeDayResult.substring(i+1);

        parseThreeDayXML();
    }

    private void parseOneDayXML() {
        synchronized (status)
        {
            try {
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                parserFactory.setNamespaceAware(false);
                XmlPullParser pullParser = parserFactory.newPullParser();
                String locName = "", windDir = null, visibility = null, conditions="";
                LatLng locLatLng = null;
                float currentTemperature = 0, highTemperature = 0, lowTemperature = 0, windSpeed = 0, pressure = 0, humidity = 0;
                DayOfWeek day = null;
                int uvRisk = 0;
                LocalTime sunset, sunrise;

                pullParser.setInput(new StringReader(oneDayResult));
                int eventType = pullParser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {

                    if (eventType == XmlPullParser.START_TAG) {
                        String temp;
                        switch (pullParser.getName()) {
                            case "title":
                                eventType = pullParser.next();
                                if (eventType == XmlPullParser.TEXT) {
                                    temp = pullParser.getText();
                                    if (temp.contains("GMT")) {

                                        String[] titleSplit = temp.split(":", 1);
                                        for (int i = 0; i < titleSplit.length; i++)
                                        {
                                            Log.d("Cheese", titleSplit[i]);
                                        }

                                        day = DayOfWeek.valueOf(titleSplit[0].split("-")[0].trim().toUpperCase());
                                        String[] condSplit = temp.split(":");

                                        conditions = condSplit[2].split(",")[0].trim();
                                    }
                                }
                                break;
                            case "description":
                                eventType = pullParser.next();
                                if (eventType == XmlPullParser.TEXT) {
                                    temp = pullParser.getText();
                                    if (temp.contains("Direction")) {
                                        String[] desSplit = temp.split(",");

                                        currentTemperature = valueFromString(desSplit[0].split(":")[1]);
                                        windDir = desSplit[1].split(":")[1].trim();


                                        //Error Handling just in case the XML produces garbage, which is occasionally does.
                                        if(Objects.equals(desSplit[2].split(":")[1].trim(), "--"))
                                        {
                                            windSpeed = Float.parseFloat(desSplit[2].split(":")[1].replace("mph", "").trim());

                                        }
                                        else
                                        {
                                            windSpeed = Float.NaN; //Really don't want to know the performance overhead on this.
                                        }

                                        if(Objects.equals(desSplit[4].split(":")[1].trim(),"--"))
                                        {
                                            pressure = Float.parseFloat(desSplit[4].split(":")[1].replace("mb", "").trim()); //There it is again

                                        }
                                        else
                                        {
                                            pressure = Float.NaN;
                                        }

                                        if(Objects.equals(desSplit[3].split(":")[1].trim(),"--"))
                                        {
                                            humidity = Float.parseFloat(desSplit[3].split(":")[1].replace("%", "").trim()); //There it is again

                                        }
                                        else
                                        {
                                            humidity = Float.NaN;

                                        }


                                        visibility = desSplit[6].split(":")[1]; //Evidently, this can produce garbage, so probably should handle it.

                                    }
                                    //I hate this.

                                }

                                break;

                        }

                    }


                    eventType = pullParser.next();
                }



                oneDayWeather = new ExtendedWeather(locName, null, currentTemperature,
                        day, highTemperature, lowTemperature, windSpeed, windDir,
                        visibility, pressure, humidity, uvRisk, null, null, conditions);

                isOneDayFinished = true;
                status.notify();
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();

            }
        }




    }
    private void parseThreeDayXML(){

        synchronized (status)
        {
            while(!isOneDayFinished)
            {
                try {
                    status.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            ArrayList<BasicWeather> weather = new ArrayList<>(3);
            ArrayList<ExtendedWeather> weatherExtended = new ArrayList<>(3);
            try
            {
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                parserFactory.setNamespaceAware(false);
                XmlPullParser pullParser = parserFactory.newPullParser();



                float maxTemp, minTemp, windSpeed, pressure, humidity;
                String windDir, locName = "", day = null, conditions="", visibility;
                int uvRisk;
                LocalTime sunrise = null, sunset = null;

                pullParser.setInput(new StringReader(threeDayResult));
                int eventType = pullParser.getEventType();
                while(eventType != XmlPullParser.END_DOCUMENT)
                {

                    if(eventType == pullParser.START_TAG) {
                        //TODO: Implement all the tags in the 3 Day, and create separate conditions depending on what we need.
                        String temp;
                        switch (pullParser.getName())
                        {
                            case "title":
                                eventType = pullParser.next();
                                if(eventType == XmlPullParser.TEXT)
                                {
                                    temp = pullParser.getText();
                                    if(temp != null && temp.contains("Minimum"))
                                    {
                                        String[] titleSplit = temp.split(":");
                                        conditions = titleSplit[1].split(",")[0].trim();
                                        day = titleSplit[0].trim().toUpperCase();
                                        if(day.equalsIgnoreCase("tonight"))
                                        {
                                            day = null;
                                        }



                                    }
                                    else if (temp != null && temp.contains("BBC"))
                                    {

                                        String[] locSplit = temp.split("-");
                                        locName = locSplit[1].split(" ")[4];
                                    }
                                }


                            case "description":
                                eventType = pullParser.next();
                                if(eventType == XmlPullParser.TEXT)
                                {
                                    temp = pullParser.getText();
                                    if(temp != null && temp.contains("Maximum")) {
                                        Log.d("TC", "Description" );
                                        String[] strSplit = temp.split(",");

                                        maxTemp = valueFromString(strSplit[0]);
                                        minTemp = valueFromString(strSplit[1]);
                                        windDir = strSplit[2].split(":")[1].trim();

                                        if(Objects.equals(strSplit[3].split(":")[1].trim(), "--"))
                                        {
                                            windSpeed = Float.NaN;
                                        }
                                        else
                                        {
                                            windSpeed = Float.parseFloat(strSplit[3].split(":")[1].replace("mph", "").trim()); //I legitimately lost passion for life writing this
                                        }

                                        visibility = strSplit[4].split(":")[1].trim();

                                        if(Objects.equals(strSplit[5].split(":")[1].trim(),"--"))
                                        {
                                            pressure = Float.NaN;
                                        }
                                        else
                                        {
                                            pressure = Float.parseFloat(strSplit[5].split(":")[1].replace("mb", "").trim());
                                        }

                                        if(Objects.equals(strSplit[6].split(":")[1].trim(),"--"))
                                        {
                                            humidity = Float.NaN;
                                        }
                                        else
                                        {
                                            humidity = Float.parseFloat(strSplit[6].split(":")[1].replace("%","").trim());
                                        }

                                        if(Objects.equals(strSplit[7].split(":")[1].trim(),"--"))
                                        {
                                            uvRisk = Integer.MAX_VALUE;
                                        }
                                        else
                                        {
                                            uvRisk = Integer.parseInt(strSplit[7].split(":")[1].trim());
                                        }

                                        if (Objects.equals(day, "TODAY") || Objects.equals(day, "TONIGHT") )
                                        {
                                            weather.add(new BasicWeather(locName, null, maxTemp, minTemp, windSpeed, windDir, null, conditions));
                                            weatherExtended.add(new ExtendedWeather(locName, null, 0, null, maxTemp, minTemp, windSpeed, windDir, visibility, pressure, humidity, uvRisk, sunrise, sunset, conditions));

                                            Log.d("TC", "Created no day" );


                                        }
                                        else
                                        {

                                            DayOfWeek d = DayOfWeek.valueOf(day.toUpperCase());

                                            weather.add(new BasicWeather(locName, null, maxTemp, minTemp, windSpeed, windDir, d , conditions));
                                            weatherExtended.add(new ExtendedWeather(locName, null, 0, DayOfWeek.valueOf(day.toUpperCase().trim()), maxTemp, minTemp, windSpeed, windDir, visibility, pressure, humidity, uvRisk, sunrise, sunset, conditions));
                                            oneDayData = new OneDayData(uvRisk, sunrise, sunset, maxTemp, minTemp);
                                            createDetailedOneDay();

                                            Log.d("TC", "else" );

                                        }

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                            sunrise = LocalTime.parse(strSplit[9].substring(10, 15).trim());
                                            sunset = LocalTime.parse(strSplit[10].substring(9, 14).trim());
                                        }













                                    }

                                }
                                break;

                        }

                    }

                    eventType = pullParser.next();

                }

                threeDayWeather = weather;
                threeDayWeatherExtended = weatherExtended;

                for (int i = 0; i < threeDayWeather.size(); i++) {
                    Log.d("Testing", Float.toString(threeDayWeather.get(i).getHighTemperature()));
                }



                correctFirstDate();


            }
            catch (XmlPullParserException e)
            {
                e.printStackTrace();

            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }

    }

    private void correctFirstDate()
    {
        if(threeDayWeather.get(1) != null)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                assert threeDayWeather.get(1).getDay() != null;
                threeDayWeather.get(0).setDay(threeDayWeather.get(1).getDay().minus(1));
                threeDayWeatherExtended.get(0).setDay(threeDayWeatherExtended.get(1).getDay().minus(1));
            }
        }

        weatherParsedListener.weatherSuccessfullyParsed(threeDayWeather, threeDayWeatherExtended, oneDayWeather);
    }
    private float valueFromString(String s){

        Pattern pattern = Pattern.compile("\\d+\\.?\\d*°C");
        Matcher matcher = pattern.matcher(s);
        if(matcher.find())
        {
            String temp = matcher.group().replace("°C", "");
            return Float.parseFloat(temp);
        }


        return Float.NaN;
    }

}
