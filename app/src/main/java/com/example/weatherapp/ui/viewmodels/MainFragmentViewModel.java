package com.example.weatherapp.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherapp.data.ExtendedWeather;
import com.example.weatherapp.models.MainModel;

public class MainFragmentViewModel extends ViewModel {
    private MainModel model;
    private MutableLiveData<ExtendedWeather> weatherMutableLiveData = new MutableLiveData<>();

    public LiveData<ExtendedWeather> retrieveWeather()
    {
        return weatherMutableLiveData;
    }

    public void setWeather(ExtendedWeather extendedWeather)
    {
        weatherMutableLiveData.setValue(extendedWeather);

    }
    public MainFragmentViewModel()
    {

        model = MainModel.getModelInstance();


        if(model.getOneDayExtended() != null)
        {
            setWeather(model.getOneDayExtended());
        }

    }




}
