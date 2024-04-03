package com.example.weatherapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class MainViewModel extends AndroidViewModel {

    //TODO: USE DEP INJECT TO GET CONTEXT FOR LOADING SHIT
    private MutableLiveData<MainModel> model;
    public MainViewModel(@NonNull Application application) {
        super(application);
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

    public ArrayList<ExtendedWeather> getThreeDayExtended()
    {
        return getData().getValue().getThreeDayExtended();
    }

    public ExtendedWeather getOneDayForecast()
    {
        return getData().getValue().getOneDayExtended();
    }

    public ArrayList<LocationRSS> getLocationFromName()
    {
        return getData().getValue().getLocationRSS(getApplication().getApplicationContext());
    }

    public String returnWeatherDirection(String value)
    {
        return getData().getValue().getWindDir(value);
    }

}