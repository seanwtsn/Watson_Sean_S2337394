package com.example.weatherapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

    public void getThreeDaySimple()
    {

    }

    public ExtendedWeather getOneDaySimple()
    {
        return model.returnOneDayWeatherSimple();
    }

    public ExtendedWeather getOneDayExtended()
    {
        return model.returnOneDayWeatherExtended();
    }

}