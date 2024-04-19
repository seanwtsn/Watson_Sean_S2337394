package com.example.weatherapp.helpers;

public class GetWindDirectionHelper
{
    public static String getWindDir(String wd)
    {
        wd = wd.toLowerCase();

        switch(wd)
        {
            case "south westerly":
                return "SW";
            case "north westerly":
                return "NW";
            case "north easterly":
                return "NE";
            case "south easterly":
                return "SE";
            case "easterly":
                return "E";
            case "northerly":
                return "N";
            case "westerly":
                return "W";
            case "southerly":
                return "S";
        }

        return "NA";
    }

}
