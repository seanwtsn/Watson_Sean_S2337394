package com.example.weatherapp.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherapp.data.LocationRSS;
import com.example.weatherapp.models.MainModel;

import java.util.ArrayList;

public class MapsViewModel extends ViewModel {

    private MainModel model;
    private MutableLiveData<ArrayList<LocationRSS>> locationRSSMutableLiveData;
    public LiveData<ArrayList<LocationRSS>> getlocationRSSLiveData()
    {
        return locationRSSMutableLiveData;
    }
    public MapsViewModel()
    {
        model = MainModel.getModelInstance();

        locationRSSMutableLiveData = (MutableLiveData<ArrayList<LocationRSS>>) model.getLocationDataRSS();
    }




}
