package com.example.weatherapp;

import com.google.android.gms.maps.model.LatLng;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class ExtendedWeather extends BasicWeather {
    private float currentTemperature;
    private float[] laterTemperatures;
    private DayOfWeek day;
    private String visibility;
    private float pressure;
    private float humidity;
    private int uvRisk;
    private LocalTime sunrise;
    private LocalTime sunset;
    public ExtendedWeather(String locationName, LatLng latLng, float currentTemperature, float[] laterTemperatures, DayOfWeek day, float highTemperature, float lowTemperature, float windSpeed, String windDirection, String visibility, float pressure, float humidity, int uvRisk, LocalTime sunrise, LocalTime sunset) {
        super(locationName, latLng, highTemperature, lowTemperature, windSpeed, windDirection, day);
        this.currentTemperature = currentTemperature;
        this.laterTemperatures = laterTemperatures;
        this.day = day;
        this.visibility = visibility;
        this.pressure = pressure;
        this.humidity = humidity;
        this.uvRisk = uvRisk;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }




    public float getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(float currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public float[] getLaterTemperatures() {
        return laterTemperatures;
    }

    public void setLaterTemperatures(float[] laterTemperatures) {
        this.laterTemperatures = laterTemperatures;
    }



    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }


    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public int getUvRisk() {
        return uvRisk;
    }

    public void setUvRisk(int uvRisk) {
        this.uvRisk = uvRisk;
    }

    public LocalTime getSunrise() {
        return sunrise;
    }

    public void setSunrise(LocalTime sunrise) {
        this.sunrise = sunrise;
    }

    public LocalTime getSunset() {
        return sunset;
    }

    public void setSunset(LocalTime sunset) {
        this.sunset = sunset;
    }
}
