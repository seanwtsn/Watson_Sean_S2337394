package com.example.weatherapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {

    private MainModel model;
    private MutableLiveData<Boolean> hasParsedRecent;

    public MainViewModel()
    {
        model = new MainModel();
    }

    public MutableLiveData<Boolean> getHasParsedRecent() {
        if(hasParsedRecent == null)
        {
            hasParsedRecent = new MutableLiveData<Boolean>();
        }
        return hasParsedRecent;
    }

    public void getThreeDayDetailed()
    {

    }

    public ArrayList<BasicWeather> getThreeDaySimple()
    {
        return model.getThreeDay();
    }

    public ExtendedWeather getOneDayForecast()
    {
        return model.getOneDayExtended();
    }



}