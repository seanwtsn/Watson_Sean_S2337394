package com.example.weatherapp;

import android.util.Log;

public class WeatherIconHelper
{
    public WeatherIconHelper()
    {

    }
    public int getWeatherIcon(String weather)
    {

        weather = weather.toLowerCase();

        Log.d("WIR", weather);

        switch (weather)
        {
            case "sunny intervals":
                return R.drawable.icon_sun_norays;
            case "light rain showers":
                return R.drawable.cloud_light_rain;
            case "light rain":
            case "partly cloudy":
            case "drizzle":
            case "heavy rain":

        }




        return Integer.parseInt(null);
    }


}
