package com.example.weatherapp.models;


import android.content.Context;
import android.util.Log;

import com.example.weatherapp.data.BasicWeather;
import com.example.weatherapp.data.ExtendedWeather;
import com.example.weatherapp.data.LocationRSS;
import com.example.weatherapp.data.tasks.FileRead;
import com.example.weatherapp.data.tasks.GetWeatherTask;

import java.util.ArrayList;

public class MainModel {
    private ExtendedWeather oneDayExtended;
    private ArrayList<BasicWeather> threeDay;
    private ArrayList<ExtendedWeather> threeDayExtended;
    private ArrayList<LocationRSS> locationRSS;

    public MainModel()
    {
        //TODO: FIX


    }

    public void doWeatherTask()
    {
        getWeather("2643123");
    }

    public String getWindDir(String wd)
    {
        wd = wd.toLowerCase();

        switch(wd)
        {
            case "south westerly":
                return "SW";
            case "north westerly":
                return "NW";
            case "north easterly":
                return "NE";
            case "south easterly":
                return "SE";
            case "easterly":
                return "E";
            case "northerly":
                return "N";
            case "westerly":
                return "W";
            case "southerly":
                return "S";
        }

        return "NA";
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
            threeDayExtended = getWeatherTask.returnThreeDayWeatherExtended();
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

    public ArrayList<ExtendedWeather> getThreeDayExtended()
    {
        return threeDayExtended;
    }
    public void setLocationRSS(ArrayList<LocationRSS> locationRSS)
    {
        this.locationRSS = locationRSS;
    }
}
