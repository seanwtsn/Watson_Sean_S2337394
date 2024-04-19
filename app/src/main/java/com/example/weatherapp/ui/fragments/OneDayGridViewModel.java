package com.example.weatherapp.ui.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherapp.data.ExtendedWeather;
import com.example.weatherapp.models.MainModel;

public class OneDayGridViewModel extends ViewModel
{
    private final MainModel model;
    public OneDayGridViewModel()
    {
        model = MainModel.getModelInstance();

        extendedWeatherMutableLiveData = (MutableLiveData<ExtendedWeather>) model.getOneDayDataExtended();
    }

    private MutableLiveData<ExtendedWeather> extendedWeatherMutableLiveData;

    public LiveData<ExtendedWeather> getExtended()
    {
        return extendedWeatherMutableLiveData;
    }



}