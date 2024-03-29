package com.example.weatherapp;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;


public class ThreeDayLargeViewModel extends ViewModel
{
    private MainModel model;
    private ArrayList<BasicWeather> threeDay;

    public ArrayList<BasicWeather> getThreeDay() {
        return threeDay;
    }

    public void setThreeDay(ArrayList<BasicWeather> threeDay) {
        this.threeDay = threeDay;
    }
    // TODO: Implement the ViewModel
}