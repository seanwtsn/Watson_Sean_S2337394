package com.example.weatherapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private MainModel model = new MainModel();
    private MutableLiveData<Boolean> hasParsedRecent;

    public MainViewModel()
    {

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
        if(model.returnOneDayWeather() == null)
        {
            model.startOneDayParse();
            model.startThreeDayParse();
        }

        return model.returnOneDayWeather();
    }

}