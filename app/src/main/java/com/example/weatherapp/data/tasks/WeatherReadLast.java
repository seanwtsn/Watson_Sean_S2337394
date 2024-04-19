package com.example.weatherapp.data.tasks;

import android.content.Context;

public class WeatherReadLast implements Runnable
{
    private static String filename = "lastweather.json";
    private final Context context;

    public WeatherReadLast(Context context)
    {
        this.context = context;
    }
    @Override
    public void run() {

    }
}
