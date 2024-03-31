package com.example.weatherapp;


import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class MainModel {
    private ExtendedWeather oneDayExtended;
    private ArrayList<BasicWeather> threeDay;
    private HashMap<String,String> locationRSS;

    public MainModel()
    {

        //TODO: FIX
        getWeather("2643123");
        locationRSS = new HashMap<String, String>()
        {
            {
                this.put("Glasgow", "2648579");
                this.put("Edinburgh", "2650225");
                this.put("Irvine", "2646032");
                this.put("Paris", "2988507");
                this.put("Lisbon", "2267057");
            }
        };
    }

    private String constructRSSURL(String key, boolean isOneDay)
    {
        Log.d("RSS", key);
        StringBuilder sb = new StringBuilder();
        if(isOneDay)
        {

            sb.append("https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/")
                    .append(key);

            Log.d("RSS", sb.toString());

            return sb.toString();

        }
        else
        {
            sb.append("https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/")
                    .append(key);

            Log.d("RSS", sb.toString());

            return sb.toString().trim();
        }
    }
    public void getWeather(String rssKey)
    {
        GetWeatherTask getWeatherTask = new GetWeatherTask(constructRSSURL(rssKey, false), constructRSSURL(rssKey, true));
        Thread data = new Thread(getWeatherTask);
        data.start();
        try
        {
            data.join();
            oneDayExtended = getWeatherTask.returnOneDayWeatherExtended();
            threeDay = getWeatherTask.returnThreeDayWeather();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }


    }

    private boolean isOneDayParsed;


    public ExtendedWeather getOneDayExtended()
    {
        return oneDayExtended;
    }

    public ArrayList<BasicWeather> getThreeDay()
    {
        return threeDay;
    }

    public HashMap<String, String> getLocationRSS() {
        return locationRSS;
    }

    public void setLocationRSS(HashMap<String, String> locationRSS)
    {
        this.locationRSS = locationRSS;
    }
}
