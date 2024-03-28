package com.example.weatherapp;


import java.util.ArrayList;

public class MainModel {

    private final String URLThree = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643123";
    private final String URLOne = "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/2643123";

    private ExtendedWeather oneDayExtended;
    private ArrayList<BasicWeather> threeDay;

    public MainModel() {
        getWeather();
    }


    public void getWeather()
    {
        GetWeatherTask getWeatherTask = new GetWeatherTask(URLThree, URLOne);
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
}
