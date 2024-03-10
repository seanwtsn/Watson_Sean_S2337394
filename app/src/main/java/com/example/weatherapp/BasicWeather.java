package com.example.weatherapp;

import com.google.android.gms.maps.model.LatLng;

//This is used for the 3 day forecast, where there is less information available, could
//implement a menu to show more detail, but this will do for now.
public class BasicWeather {
    private String locationName;
    private LatLng latLng;
    private float highTemperature;
    private float lowTemperature;
    private float windSpeed;
    private String windDirection;

    public BasicWeather(String locationName, LatLng latLng, float highTemperature, float lowTemperature, float windSpeed, String windDirection){
        this.locationName = locationName;
        this.latLng = latLng;
        this.highTemperature = highTemperature;
        this.lowTemperature = lowTemperature;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public float getHighTemperature() {
        return highTemperature;
    }

    public void setHighTemperature(float highTemperature) {
        this.highTemperature = highTemperature;
    }

    public float getLowTemperature() {
        return lowTemperature;
    }

    public void setLowTemperature(float lowTemperature) {
        this.lowTemperature = lowTemperature;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }


}
