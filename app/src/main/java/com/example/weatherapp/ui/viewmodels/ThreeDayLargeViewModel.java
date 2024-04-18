package com.example.weatherapp.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherapp.data.ExtendedWeather;
import com.example.weatherapp.models.MainModel;

import java.util.ArrayList;


public class ThreeDayLargeViewModel extends ViewModel
{
    private final MainModel mainModel;
    private MutableLiveData<ArrayList<ExtendedWeather>> mainModelMutableLiveData = new MutableLiveData<>();
    public ThreeDayLargeViewModel()
    {
        mainModel = MainModel.getModelInstance();

        mainModelMutableLiveData = (MutableLiveData<ArrayList<ExtendedWeather>>) mainModel.getThreeDayDataExtended();
    }
    public LiveData<ArrayList<ExtendedWeather>> getData()
    {
        return mainModelMutableLiveData;
    }





}