package com.example.weatherapp.data.tasks;

import android.content.Context;

import com.example.weatherapp.data.BasicWeather;
import com.example.weatherapp.data.ExtendedWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class WeatherWriteLast implements Runnable {

    private static String filename = "lastweather.json";
    private final Context context;
    private ArrayList<ExtendedWeather> threeDayExtended;
    private ArrayList<BasicWeather> threeDaySmall;
    private ExtendedWeather oneDay;
    public WeatherWriteLast(Context context, ArrayList<ExtendedWeather> threeDayExtended, ArrayList<BasicWeather> threeDaySmall, ExtendedWeather oneDay)
    {
        this.context = context;
        this.threeDayExtended = threeDayExtended;
        this.threeDaySmall = threeDaySmall;
        this.oneDay = oneDay;
    }

    private void saveWeathers(ArrayList<ExtendedWeather> threeDayExtended, ArrayList<BasicWeather> threeDaySmall, ExtendedWeather oneDay)
    {
        File fileDir = context.getFilesDir();
        if (!context.getDir(filename, context.MODE_PRIVATE).exists())
        {
            fileDir.mkdir();
        }
        JSONArray threeDayExtendedJsonArray = new JSONArray(threeDayExtended);
        JSONArray threeDayJsonArray = new JSONArray(threeDaySmall);
        JSONObject oneDayJsonObject = new JSONObject();

        if(LoadJSON() != null)
        {
            try {

                oneDayJsonObject.put("oneDay", oneDay);

                /*
                oneDayJsonObject.put("locationName", oneDay.getLocationName());
                oneDayJsonObject.put("latLng", oneDay.getLatLng());
                oneDayJsonObject.put("currentTemperature", oneDay.getCurrentTemperature());
                oneDayJsonObject.put("day", oneDay.getDay());
                oneDayJsonObject.put("visibility", oneDay.getVisibility());
                oneDayJsonObject.put("pressure", oneDay.getPressure());
                oneDayJsonObject.put("humidity", oneDay.getHumidity());
                oneDayJsonObject.put("uvRisk", oneDay.getUvRisk());
                oneDayJsonObject.put("sunrise", oneDay.getSunrise());
                oneDayJsonObject.put("sunset", oneDay.getSunset());
                */
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }
        else
        {
            FileOutputStream outputStreamWriter = null;
            try {
                outputStreamWriter = context.openFileOutput(filename, Context.MODE_PRIVATE);
                JSONObject j = new JSONObject();
                j.put("threeDay", threeDayJsonArray);
                j.put("threeDayExtended", threeDayExtendedJsonArray);
                j.put("oneDay", oneDayJsonObject);
                outputStreamWriter.write(j.toString().getBytes());
                outputStreamWriter.flush();
                outputStreamWriter.close();
            }
            catch (JSONException | IOException e)
            {
                throw new RuntimeException(e);
            }
        }


    }

    public String LoadJSON()
    {
        String json = null;
        File fileDir = context.getFilesDir();
        File file = new File(fileDir, filename);
        if(file.exists())
        {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                int size = fileInputStream.available();
                byte[] buffer = new byte[size];
                fileInputStream.read(buffer);
                fileInputStream.close();
                json = new String(buffer, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if(json.isEmpty()){
                return null;
            }
            return json;
        }
        return null;
    }
    @Override
    public void run()
    {
        saveWeathers(threeDayExtended, threeDaySmall, oneDay);
    }
}
