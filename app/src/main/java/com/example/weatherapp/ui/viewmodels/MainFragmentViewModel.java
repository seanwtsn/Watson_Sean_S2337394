package com.example.weatherapp.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.example.weatherapp.data.ExtendedWeather;
import com.example.weatherapp.models.MainModel;

public class MainFragmentViewModel extends ViewModel {
    private MainModel model;
    public MutableLiveData<ExtendedWeather> weatherMutableLiveData = new MutableLiveData<>();
    public LiveData<ExtendedWeather> retrieveWeather()
    {
        return weatherMutableLiveData;
    }

    public void setWeather(ExtendedWeather extendedWeather)
    {
        weatherMutableLiveData.setValue(extendedWeather);
    }
    public MainFragmentViewModel(MainModel model)
    {
        this.model = model;

        if(this.model.getOneDayExtended() != null)
        {
            setWeather(model.getOneDayExtended());
        }
    }

    public static final ViewModelInitializer<MainFragmentViewModel> initializer = new ViewModelInitializer<>(MainFragmentViewModel.class, creationExtras -> {

        MainModel model = MainModel.getModelInstance();
        assert model != null;
        return new MainFragmentViewModel(model);

    });

}
