package com.example.weatherapp.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherapp.models.MainModel;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<MainModel> mainModelMutableLiveData;
    public MutableLiveData<MainModel> getMainModelMutableLiveData()
    {
        if(mainModelMutableLiveData == null)
        {
            mainModelMutableLiveData = new MutableLiveData<>(MainModel.getModelInstance());
            return mainModelMutableLiveData;
        }
        return mainModelMutableLiveData;
    }
    public MainActivityViewModel()
    {
        getMainModelMutableLiveData();

    }


    public LiveData<MainModel> getModel()
    {
        return getMainModelMutableLiveData();
    }



}