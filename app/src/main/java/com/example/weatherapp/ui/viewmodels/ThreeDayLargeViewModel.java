package com.example.weatherapp.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherapp.data.BasicWeather;
import com.example.weatherapp.models.MainModel;

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
    public ArrayList<BasicWeather> getThreeDaySimple()
    {
        return getData().getValue().getThreeDay();
    }

    public ArrayList<BasicWeather> threeDay;


}