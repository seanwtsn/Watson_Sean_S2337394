package com.example.weatherapp.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherapp.data.LocationRSS;
import com.example.weatherapp.models.MainModel;

import java.util.ArrayList;

public class WeatherViewModel extends ViewModel
{
    private MutableLiveData<ArrayList<LocationRSS>> listMutableLiveData = new MutableLiveData<>();

    public LiveData<ArrayList<LocationRSS>> getList()
    {
        return listMutableLiveData;
    }

    public void setListMutableLiveData(ArrayList<LocationRSS> listMutableLiveData)
    {
        this.listMutableLiveData.setValue(listMutableLiveData);
    }

    public WeatherViewModel()
    {
        MainModel model = MainModel.getModelInstance();
        if(model.isReadSucessfully())
        {
            listMutableLiveData = (MutableLiveData<ArrayList<LocationRSS>>) model.getLocationDataRSS();
        }
    }
}
