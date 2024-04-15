package com.example.weatherapp.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherapp.data.ExtendedWeather;
import com.example.weatherapp.models.MainModel;

public class OneDayViewModel extends ViewModel
{
    private final MainModel model;
    private MutableLiveData<ExtendedWeather> extendedWeatherMutableLiveData = new MutableLiveData<>();

    public OneDayViewModel() {
        this.model = MainModel.getModelInstance();
        if(model.getOneDayExtended() != null)
        {
            setExtendedWeather(model.getOneDayExtended());
        }

    }

    public void setExtendedWeather(ExtendedWeather extendedWeather) {
        extendedWeatherMutableLiveData.setValue(extendedWeather);
    }

    public LiveData<ExtendedWeather> getOneDayWeather()
    {
        return extendedWeatherMutableLiveData;
    }


}
