package com.example.weatherapp;

import com.google.android.gms.maps.model.LatLng;

public class LocationRSS {
    private String name;
    private String countryCode;
    private LatLng position;
    private String rssKey;

    public LocationRSS(String name, String countryCode, LatLng position, String rssKey)
    {
        this.name = name;
        this.countryCode = countryCode;
        this.position = position;
        this.rssKey = rssKey;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public String getRssKey() {
        return rssKey;
    }

    public void setRssKey(String rssKey) {
        this.rssKey = rssKey;
    }
}
