package com.example.weatherapp;


import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class MainModel {
    private ExtendedWeather oneDayExtended;
    private ArrayList<BasicWeather> threeDay;
    private ArrayList<LocationRSS> locationRSS;

    public MainModel()
    {
        //TODO: FIX
        getWeather("2643123");

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

    public ArrayList<LocationRSS> getLocationRSS(Context context) {
        if(locationRSS != null)
        {
            return locationRSS;
        }
        else
        {
            FileRead fileRead = new FileRead(context);
            Thread t = new Thread(fileRead);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return fileRead.getLocations();
        }
    }

    public ExtendedWeather getOneDayExtended()
    {
        return oneDayExtended;
    }

    public ArrayList<BasicWeather> getThreeDay()
    {
        return threeDay;
    }
    public void setLocationRSS(ArrayList<LocationRSS> locationRSS)
    {
        this.locationRSS = locationRSS;
    }
}
