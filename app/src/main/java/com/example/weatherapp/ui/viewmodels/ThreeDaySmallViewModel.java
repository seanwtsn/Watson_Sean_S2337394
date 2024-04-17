package com.example.weatherapp.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherapp.data.BasicWeather;
import com.example.weatherapp.models.MainModel;

import java.util.ArrayList;

public class ThreeDaySmallViewModel extends ViewModel
{
    private final MainModel model;
    private MutableLiveData<ArrayList<BasicWeather>> threeDaySmallMuteData = new MutableLiveData<>();

    public LiveData<ArrayList<BasicWeather>> threeDaySmallLiveData()
    {
        return threeDaySmallMuteData;
    }

    public void setThreeDaySmallData(ArrayList<BasicWeather> weather)
    {
        this.threeDaySmallMuteData.setValue(weather);
    }

    public ThreeDaySmallViewModel()
    {
        model = MainModel.getModelInstance();

        threeDaySmallMuteData = (MutableLiveData<ArrayList<BasicWeather>>) model.getThreeDayData();

        if(model.getThreeDay() != null)
        {
            setThreeDaySmallData(model.getThreeDay());
        }
    }

}
