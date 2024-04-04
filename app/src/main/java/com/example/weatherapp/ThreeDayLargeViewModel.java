package com.example.weatherapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;


public class ThreeDayLargeViewModel extends ViewModel
{
    private MutableLiveData<MainModel> model;

    public ThreeDayLargeViewModel()
    {
        model = new MutableLiveData<>(new MainModel());
    }

    public LiveData<MainModel> getData()
    {



        return model;
    }

    public void weather()
    {
        getData().getValue().doWeatherTask();
    }
    public ArrayList<BasicWeather> getThreeDaySimple()
    {
        return getData().getValue().getThreeDay();
    }

    public ArrayList<BasicWeather> threeDay;


}