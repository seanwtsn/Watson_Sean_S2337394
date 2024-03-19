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

public class MainModel {
    public MainModel()
    {

    }
    private final String urlThreeDaySource = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643123";
    private final String urlOneDaySource = "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/2643123";
    private String result;
    private ArrayList<BasicWeather> threeDayWeather;
    private ExtendedWeather oneDayWeather;
    private OneDayData oneDayData;
    public ArrayList<BasicWeather> returnThreeDayWeather() {
        return threeDayWeather;
    }
    public ExtendedWeather returnOneDayWeather() { return oneDayWeather; }
    public void startThreeDayParse()
    {
        new Thread(new GetThreeDayTask(urlThreeDaySource)).start();
    }
    public void startOneDayParse()
    {
        new Thread(new GetOneDayTask(urlOneDaySource)).start();
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

    private void createDetailedOneDay()
    {
        oneDayWeather.setUvRisk(oneDayData.getUvRisk());
        oneDayWeather.setHighTemperature(oneDayData.getMaxTemperature());
        oneDayWeather.setLowTemperature(oneDayData.getMinTemperature());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            oneDayWeather.setSunrise(oneDayData.getSunrise());
            oneDayWeather.setSunset(oneDayData.getSunset());
        }
    }

    private class GetOneDayTask implements Runnable
    {
        private final String url;
        public GetOneDayTask(String url)
        {
            this.url = url;
        }
        @Override
        public void run()
        {
            URL aurl;
            URLConnection _url;
            BufferedReader in = null;
            String inputLine = "";
            Log.d("Testing", "run() called");
            try
            {
                aurl = new URL(url);
                _url = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader((_url.getInputStream())));
                while((inputLine = in.readLine()) != null){
                    result = result + inputLine;
                }
                in.close();
            }
            catch (IOException e)
            {

            }
            int i = result.indexOf(">");
            result = result.substring(i+1);
            i = result.indexOf(">");
            result = result.substring(i+1);

            Log.d("Testing", result);

            parseOneDayXML();
        }
        private void parseOneDayXML()
        {
            try
            {
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                parserFactory.setNamespaceAware(false);
                XmlPullParser pullParser = parserFactory.newPullParser();
                String locName = "", windDir = null, visibility = null;
                LatLng locLatLng = null;
                float currentTemperature = 0,  highTemperature = 0, lowTemperature = 0, windSpeed = 0, pressure = 0, humidity = 0;
                DayOfWeek day = null;
                int uvRisk = 0;
                LocalTime sunset, sunrise;

                pullParser.setInput(new StringReader(result));
                int eventType = pullParser.getEventType();
                while(eventType != XmlPullParser.END_DOCUMENT)
                {
                    if(eventType == XmlPullParser.START_TAG)
                    {
                        String temp;
                        switch(pullParser.getName())
                        {
                            case "title":
                                temp = pullParser.nextText();
                                if(temp.contains("GMT"))
                                {
                                    String[] titleSplit = temp.split(":", 1);
                                    day = DayOfWeek.valueOf(titleSplit[0].split("-")[0]);
                                }
                                case "description":
                                temp = pullParser.nextText();
                                if(temp.contains("Direction"))
                                {
                                    String[] desSplit = temp.split(",");
                                    currentTemperature = valueFromString(desSplit[0].split(":")[1]);
                                    windDir = desSplit[1].split(":")[1].trim();
                                    windSpeed = Float.parseFloat(desSplit[2].split(":")[1].replace("mph", "").trim()); //Really don't want to know the performance overhead on this.
                                    humidity = Float.parseFloat(desSplit[3].split(":")[1].replace("%", "").trim()); //There it is again
                                    pressure = Float.parseFloat(desSplit[4].split(":")[1].replace("mb", "").trim()); //There it is again
                                    visibility = desSplit[6].split(":")[1]; //Evidently, this can produce garbage, so probably should handle it.

                                }
                                //I hate this.
                                oneDayWeather = new ExtendedWeather(locName, null, currentTemperature,
                                        null, day, highTemperature, lowTemperature, windSpeed, windDir,
                                        visibility, pressure, humidity, uvRisk, null, null);

                                Log.d("1D Test", locName);
                                Log.d("1D Test", Float.toString(currentTemperature));
                                Log.d("1D Test", Float.toString(highTemperature));
                                Log.d("1D Test", Float.toString(lowTemperature));
                                Log.d("1D Test", Float.toString(windSpeed));
                                Log.d("1D Test", Float.toString(pressure));
                                Log.d("1D Test", Float.toString(humidity));
                                Log.d("1D Test", Integer.toString(uvRisk));
                                break;

                        }

                    }
                    eventType = pullParser.next();
                }

            }
            catch (XmlPullParserException | IOException e)
            {
                e.printStackTrace();

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
    private class GetThreeDayTask implements Runnable
    {

        private final String url;

        public GetThreeDayTask(String aurl){
            url = aurl;
        }

        @Override
        public void run() {
            URL aurl;
            URLConnection _url;
            BufferedReader in = null;
            String inputLine = "";
            Log.d("Testing", "3day run() called");
            try
            {
                aurl = new URL(url);
                _url = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader((_url.getInputStream())));
                while((inputLine = in.readLine()) != null){
                    result = result + inputLine;
                }
                in.close();
            }
            catch (IOException e)
            {

            }
            int i = result.indexOf(">");
            result = result.substring(i+1);
            i = result.indexOf(">");
            result = result.substring(i+1);

            Log.d("Testing", result);

            parseThreeDayXML();


        }

        private void parseThreeDayXML(){
            ArrayList<BasicWeather> weather = new ArrayList<>(3);
            Log.d("Testing", "parseThreeDayXML run ");
            try
            {
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                parserFactory.setNamespaceAware(false);
                XmlPullParser pullParser = parserFactory.newPullParser();

                float maxTemp, minTemp, windSpeed;
                String windDir, locName = "", day = "";

                pullParser.setInput(new StringReader(result));
                int eventType = pullParser.getEventType();
                while(eventType != XmlPullParser.END_DOCUMENT)
                {
                    if(eventType == pullParser.START_TAG) {
                        //TODO: Implement all the tags in the 3 Day, and create separate conditions depending on what we need.
                        String temp;
                        switch (pullParser.getName())
                        {
                            case "title":
                                temp = pullParser.nextText();
                                Log.d("Testing", "parseThreeDayXML: title called");
                                if(temp != null && temp.contains("Minimum"))
                                {
                                    Log.d("Testing", "parseThreeDayXML: minimum condition called");

                                    String[] titleSplit = temp.split(":");

                                    Log.d("Testing", titleSplit[0]);

                                    if(!titleSplit[0].trim().equalsIgnoreCase("today"))
                                    {
                                        Log.d("Testing", "parseThreeDayXML: day condition called");

                                        day = titleSplit[0].trim().toUpperCase();
                                    }
                                }
                                else if (temp != null && temp.contains("BBC"))
                                {
                                    Log.d("Testing", "parseThreeDayXML: location condition called");

                                    String[] locSplit = temp.split("-");
                                    locName = locSplit[1].split(" ")[4];
                                }
                                break;

                            case "description":
                                temp = pullParser.nextText();
                                Log.d("Testing", "parseThreeDayXML: description called");
                                Log.d("Testing", temp);
                                if(temp != null && temp.contains("Maximum"))
                                {
                                    String[] strSplit = temp.split(",");
                                    maxTemp = valueFromString(strSplit[0]);
                                    minTemp = valueFromString(strSplit[1]);
                                    windDir = strSplit[2].split(":")[1].trim();
                                    windSpeed = Float.parseFloat(strSplit[3].split(":")[1].replace("mph", "").trim()); //I legitimately lost passion for life writing this

                                    if(day == null || day.equalsIgnoreCase("tonight"))
                                    {
                                        int uvRisk;
                                        LocalTime sunrise = null, sunset = null;
                                        float maxTemperature, minTemperature;

                                        maxTemperature = maxTemp;
                                        minTemperature = minTemp;

                                        uvRisk = Integer.parseInt(strSplit[7].split(":")[1]);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                        {
                                            sunrise = LocalTime.parse(strSplit[9].split(":")[1]);
                                            sunset = LocalTime.parse(strSplit[10].split(":")[1]);

                                        }

                                        weather.add(new BasicWeather(locName, null, maxTemp, minTemp, windSpeed, windDir, null));
                                        oneDayData = new OneDayData(uvRisk, sunrise, sunset, maxTemperature, minTemperature);
                                        createDetailedOneDay();

                                    }
                                    else
                                    {
                                        weather.add(new BasicWeather(locName, null, maxTemp, minTemp, windSpeed, windDir, DayOfWeek.valueOf(day.toUpperCase())));
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
            catch (XmlPullParserException ignore)
            {


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
