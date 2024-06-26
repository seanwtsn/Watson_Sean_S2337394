package com.example.weatherapp.helpers;

import android.util.Log;

import com.example.weatherapp.R;

public class WeatherIconHelper
{
    public WeatherIconHelper()
    {

    }
    public static int getWeatherIcon(String weather)
    {

        weather = weather.toLowerCase();

        Log.d("WIR", weather);

        switch (weather)
        {
            case "sunny intervals":
            case "clear sky":
                return R.drawable.icon_sun_norays;
            case "light rain showers":
            case "light rain":
                return R.drawable.cloud_light_rain;
            case "partly cloudy":
                return R.drawable.partly_cloudy;
            case "drizzle":
            case "heavy rain":
                return R.drawable.cloud_heavy_rain;
            case "light cloud":
                return R.drawable.icon_cloud;
        }




        return 0;
    }


}
