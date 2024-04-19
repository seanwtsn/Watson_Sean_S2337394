package com.example.weatherapp.helpers;

import com.google.android.gms.maps.model.LatLng;

public class ReturnClosestLocationHelper
{
    public static double getDistance(LatLng currentPos, LatLng closestPos)
    {
        double closestLat = Math.toRadians(closestPos.latitude);
        double closestLon = Math.toRadians(closestPos.longitude);

        double userLat = Math.toRadians(currentPos.latitude);
        double userLon = Math.toRadians(currentPos.longitude);

        double latDelta = userLat - closestLat;
        double lonDelta = userLon - closestLon;

        //Haversine Formula for getting a distance between two points on a sphere
        double h = Math.pow(Math.sin(latDelta/2), 2) + Math.cos(closestLat) * Math.cos(userLat) * Math.pow(Math.sin(lonDelta / 2), 2);
        double a = 2 * Math.atan2(Math.sqrt(h), Math.sqrt(1 - h));

        //6371 being the radius of the earth in KM
        return 6371 * a;

    }


}
