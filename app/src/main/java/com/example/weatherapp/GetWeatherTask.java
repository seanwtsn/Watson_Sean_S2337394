package com.example.weatherapp;

import android.os.Build;
import android.util.Log;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetWeatherTask implements Runnable
{
    public GetWeatherTask(String urlThree, String urlOne)
    {
        this.urlThree = urlThree;
        this.urlOne = urlOne;
    }
    private final String urlOne;
    private final String urlThree;
    private String oneDayResult;
    private String threeDayResult;
    private ArrayList<BasicWeather> threeDayWeather;
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
    public ArrayList<BasicWeather> returnThreeDayWeather() {
        return threeDayWeather;
    }
    public ExtendedWeather returnOneDayWeatherSimple()
    {
        return oneDayWeather;
    }

    public ExtendedWeather returnOneDayWeatherExtended()
    {
        return OneDayWeatherExtended;
    }
    private ExtendedWeather createDetailedOneDay()
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

            return OneDayWeatherExtended;
        }
        else
        {
            Log.d("ERROR", "oneDayData = Null");
        }

        return null;

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
            }
            in.close();
        }
        catch (IOException e)
        {

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
                String locName = "", windDir = null, visibility = null;
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
                                        day = DayOfWeek.valueOf(titleSplit[0].split("-")[0].trim().toUpperCase());
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

                                        windSpeed = Float.parseFloat(desSplit[2].split(":")[1].replace("mph", "").trim()); //Really don't want to know the performance overhead on this.
                                        pressure = Float.parseFloat(desSplit[4].split(":")[1].replace("mb", "").trim()); //There it is again
                                        humidity = Float.parseFloat(desSplit[3].split(":")[1].replace("%", "").trim()); //There it is again
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
                        null, day, highTemperature, lowTemperature, windSpeed, windDir,
                        visibility, pressure, humidity, uvRisk, null, null);

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
            try
            {
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                parserFactory.setNamespaceAware(false);
                XmlPullParser pullParser = parserFactory.newPullParser();

                float maxTemp, minTemp, windSpeed;
                String windDir, locName = "", day = "";

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
                                        if(!titleSplit[0].trim().equalsIgnoreCase("today"))
                                        {
                                            day = titleSplit[0].trim().toUpperCase();
                                        }
                                        else
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
                                break;

                            case "description":
                                eventType = pullParser.next();
                                if(eventType == XmlPullParser.TEXT)
                                {
                                    temp = pullParser.getText();
                                    if(temp != null && temp.contains("Maximum")) {
                                        String[] strSplit = temp.split(",");
                                        maxTemp = valueFromString(strSplit[0]);
                                        minTemp = valueFromString(strSplit[1]);
                                        windDir = strSplit[2].split(":")[1].trim();
                                        windSpeed = Float.parseFloat(strSplit[3].split(":")[1].replace("mph", "").trim()); //I legitimately lost passion for life writing this


                                        if (day == null || day.isEmpty() || day.equalsIgnoreCase("tonight") || day.equalsIgnoreCase("today")) {


                                            int uvRisk;
                                            LocalTime sunrise = null, sunset = null;
                                            float maxTemperature, minTemperature;

                                            maxTemperature = maxTemp;
                                            minTemperature = minTemp;

                                            uvRisk = Integer.parseInt(strSplit[7].split(":")[1].trim());
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                                sunrise = LocalTime.parse(strSplit[9].substring(10, 15).trim());
                                                sunset = LocalTime.parse(strSplit[10].substring(9, 14).trim());
                                            }

                                            weather.add(new BasicWeather(locName, null, maxTemp, minTemp, windSpeed, windDir, null));
                                            oneDayData = new OneDayData(uvRisk, sunrise, sunset, maxTemperature, minTemperature);
                                            createDetailedOneDay();
                                            Log.d("T", "CREATE 1D");
                                        }
                                        else
                                        {

                                            weather.add(new BasicWeather(locName, null, maxTemp, minTemp, windSpeed, windDir, DayOfWeek.valueOf(day.toUpperCase())));
                                        }
                                    }

                                }
                                break;
                        }

                    }

                    eventType = pullParser.next();

                }

                threeDayWeather = weather;

                for (int i = 0; i < threeDayWeather.size(); i++)
                {
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
            Log.d("Condition Testing", "correctFirstDate: called");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                assert threeDayWeather.get(1).getDay() != null;
                Log.d("Condition Testing", "correctFirstDate: Build.Version Called");
                threeDayWeather.get(0).setDay(threeDayWeather.get(1).getDay().minus(1));
            }
        }
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