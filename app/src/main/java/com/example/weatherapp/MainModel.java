package com.example.weatherapp;


import java.util.ArrayList;
import java.util.HashMap;

public class MainModel {

    private final String URLThree = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2648579";
    private final String URLOne = "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/2648579";
    private ExtendedWeather oneDayExtended;
    private ArrayList<BasicWeather> threeDay;
    public MainModel() {
        getWeather();
    }
    private HashMap<String,String> locationRSS = new HashMap<String, String>()
    {
        {
            this.put("Glasgow", "2648579");
            this.put("Edinburgh", "2650225");
            this.put("Irvine", "2646032");
            this.put("Paris", "2988507");
            this.put("Lisbon", "2267057");
        }
    };
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

    public HashMap<String, String> getLocationRSS() {
        return locationRSS;
    }

    public void setLocationRSS(HashMap<String, String> locationRSS) {
        this.locationRSS = locationRSS;
    }
}
