package com.example.weatherapp;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class ExtendedWeather extends BasicWeather {

    //Had to set some of these to nullable because stupidly
    //The one day weather misses out a lot of detail that is
    //present in the 3-day, so ill likely just grab it from index 0.
    //of the 3-day arraylist.

    @Nullable private float currentTemperature;
    private DayOfWeek day;
    private String visibility;
    private float pressure;
    private float humidity;
    @Nullable private int uvRisk;
    @Nullable private LocalTime sunrise;
    @Nullable private LocalTime sunset;
    public ExtendedWeather(String locationName, LatLng latLng, float currentTemperature, DayOfWeek day, float highTemperature, float lowTemperature, float windSpeed, String windDirection, String visibility, float pressure, float humidity, int uvRisk, LocalTime sunrise, LocalTime sunset, String conditions) {
        super(locationName, latLng, highTemperature, lowTemperature, windSpeed, windDirection, day, conditions);
        this.currentTemperature = currentTemperature;
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
