package com.example.weatherapp.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherapp.data.ExtendedWeather;
import com.example.weatherapp.models.MainModel;

public class FirstFragmentViewModel extends ViewModel {
    private MainModel model;
    private MutableLiveData<ExtendedWeather> weatherMutableLiveData = new MutableLiveData<>();

    public LiveData<ExtendedWeather> retrieveWeather()
    {
        return weatherMutableLiveData;
    }

    public void setWeather(ExtendedWeather extendedWeather)
    {
        weatherMutableLiveData.postValue(extendedWeather);
    }
    public FirstFragmentViewModel()
    {

        model = MainModel.getModelInstance();
        weatherMutableLiveData = (MutableLiveData<ExtendedWeather>) model.getOneDayDataExtended();

        if(model.getOneDayExtended() != null)
        {
            setWeather(model.getOneDayExtended());
        }

    }




}
