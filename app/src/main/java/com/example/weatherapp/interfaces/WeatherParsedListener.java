package com.example.weatherapp.interfaces;

import com.example.weatherapp.data.BasicWeather;
import com.example.weatherapp.data.ExtendedWeather;

import java.util.ArrayList;

public interface WeatherParsedListener {

    public void weatherSuccessfullyParsed(ArrayList<BasicWeather> threeDayBasic, ArrayList<ExtendedWeather> threeDayExtended, ExtendedWeather oneDayExtended);

    public void weatherUnsuccessfullyParsed();
}
