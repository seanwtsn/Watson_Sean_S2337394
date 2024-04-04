package com.example.weatherapp.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weatherapp.data.BasicWeather;
import com.example.weatherapp.data.ExtendedWeather;
import com.example.weatherapp.data.LocationRSS;
import com.example.weatherapp.models.MainModel;

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
        if(getData().getValue().getThreeDay() == null)
        {
            getData().getValue().doWeatherTask();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return getData().getValue().getThreeDay();
        }
        else
        {
            return getData().getValue().getThreeDay();
        }
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