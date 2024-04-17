package com.example.weatherapp.models;


import android.content.Context;
import android.util.Log;

import com.example.weatherapp.FileReadCallBack;
import com.example.weatherapp.WeatherParsedListener;
import com.example.weatherapp.data.BasicWeather;
import com.example.weatherapp.data.ExtendedWeather;
import com.example.weatherapp.data.LocationRSS;
import com.example.weatherapp.data.tasks.FileRead;
import com.example.weatherapp.data.tasks.GetWeatherTask;
import com.example.weatherapp.helpers.ReturnClosestLocationHelper;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainModel implements WeatherParsedListener, FileReadCallBack {

    //Yes im using a singleton pattern and yes I hate it.
    private static MainModel modelInstance;

    public static synchronized MainModel getModelInstance()
    {
        if(modelInstance == null)
        {
            modelInstance = new MainModel();
        }
        return modelInstance;
    }
    private ExtendedWeather oneDayExtended;
    private ArrayList<BasicWeather> threeDay;
    private ArrayList<ExtendedWeather> threeDayExtended;
    private ArrayList<LocationRSS> locationRSS;
    private boolean isParsed = false;

    public MainModel()
    {

    }
    public void doWeatherTask(String rss)
    {
        getWeather(rss);
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
        StringBuilder sb = new StringBuilder();
        if(isOneDay)
        {

            sb.append("https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/")
                    .append(key);



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
        GetWeatherTask getWeatherTask = new GetWeatherTask(this, constructRSSURL(rssKey, false), constructRSSURL(rssKey, true));
        Thread data = new Thread(getWeatherTask);
        data.start();
    }
    public void getLocationRSS(Context context)
    {
        FileRead fileRead = new FileRead(this, context);
        Thread t = new Thread(fileRead);
        t.start();
    }

    public ArrayList<LocationRSS> getLocationRSS()
    {
        if(locationRSS != null || !locationRSS.isEmpty())
        {
            return locationRSS;
        }
        return null;
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
    public String getRSSKeyFromLocation(LatLng location)
    {
        if(locationRSS != null)
        {
            ReturnClosestLocationHelper closestLocationHelper = new ReturnClosestLocationHelper();
            double temp = 1f;
            int index = 0;

            for (int i = 0; i < locationRSS.size(); i++) {
                if (closestLocationHelper.getDistance(location, locationRSS.get(i).getPosition()) < temp) {
                    temp = closestLocationHelper.getDistance(location, locationRSS.get(i).getPosition());
                    index = i;
                }

            }
            return locationRSS.get(index).getRssKey();
        }

        return "2648579";
    }
    @Override
    public void weatherSuccessfullyParsed(ArrayList<BasicWeather> threeDayBasic, ArrayList<ExtendedWeather> threeDayExtended, ExtendedWeather oneDayExtended)
    {
        this.threeDayExtended = threeDayExtended;
        this.threeDay = threeDayBasic;
        this.oneDayExtended = oneDayExtended;
        isParsed = true;

    }

    @Override
    public void weatherUnsuccessfullyParsed()
    {

    }
    @Override
    public void fileReadSuccessful(ArrayList<LocationRSS> list)
    {
        locationRSS = list;
        Log.d("RSS FR", "fileReadSuccessfully");
    }

    public boolean isParsed() {
        return isParsed;
    }
}
