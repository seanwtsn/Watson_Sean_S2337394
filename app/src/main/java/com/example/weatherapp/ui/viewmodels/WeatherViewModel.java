package com.example.weatherapp.ui.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weatherapp.data.LocationRSS;
import com.example.weatherapp.models.MainModel;

import java.util.ArrayList;

public class WeatherViewModel extends AndroidViewModel
{
    private MutableLiveData<ArrayList<LocationRSS>> listMutableLiveData;

    private final MainModel model;
    public LiveData<ArrayList<LocationRSS>> getList()
    {
        return listMutableLiveData;
    }

    public void setListMutableLiveData(ArrayList<LocationRSS> listMutableLiveData)
    {
        this.listMutableLiveData.setValue(listMutableLiveData);
    }

    public WeatherViewModel(Application application)
    {
        super(application);
        model = MainModel.getModelInstance();
        if(model.getLocationRSS(application.getApplicationContext()) != null)
        {
            setListMutableLiveData(model.getLocationRSS(application.getApplicationContext()));
        }
    }
}
