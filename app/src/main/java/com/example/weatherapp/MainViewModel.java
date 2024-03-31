package com.example.weatherapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<MainModel> model;

    public MainViewModel()
    {
        model = new MutableLiveData<>(new MainModel());
    }

    public LiveData<MainModel> getData()
    {
        return model;
    }
    public ArrayList<BasicWeather> getThreeDaySimple()
    {
        return getData().getValue().getThreeDay();
    }

    public ExtendedWeather getOneDayForecast()
    {
        return getData().getValue().getOneDayExtended();
    }

    public HashMap<String, String> getLocationFromName()
    {
        return getData().getValue().getLocationRSS();
    }
}