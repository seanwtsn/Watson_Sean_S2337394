package com.example.weatherapp.data.tasks;

import android.content.Context;

import com.example.weatherapp.interfaces.FileReadCallBack;
import com.example.weatherapp.data.LocationRSS;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class FileRead implements Runnable
{
    //write alarm to file
    private final FileReadCallBack fileReadCallBack;
    private static final String filename = "locations.json";
    private final Context context;
    public FileRead (FileReadCallBack fileReadCallBack, Context context){

        this.fileReadCallBack = fileReadCallBack;
        this.context = context;
    }
    @Override
    public void run() {
        loadLocations();
    }

    private void loadLocations(){
        ArrayList<LocationRSS> locations = null;
        try
        {
            if(LoadJSON() != null || !LoadJSON().isEmpty())
            {
                JSONObject obj = new JSONObject(LoadJSON());
                JSONArray locationArray = obj.getJSONArray("locations");
                locations = new ArrayList<>();
                for(int i = 0; i < locationArray.length(); i++)
                {
                    JSONObject location = locationArray.getJSONObject(i);
                    //TODO: IMPLEMENT DATA STRUCTURE
                    String locName = location.getString("name");
                    String countryCode = location.getString("countryCode");
                    LatLng pos = stringToLatLng(location.getString("position"));
                    String rss = location.getString("rssKey");

                    LocationRSS t = new LocationRSS(locName, countryCode, pos, rss);
                    locations.add(t);

                }
            }

            fileReadCallBack.fileReadSuccessful(locations);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
    }

    private LatLng stringToLatLng(String data){
        String[] parts = data.split(",");
        String part_one = parts[0];
        String part_two = parts[1];

        return new LatLng(Double.parseDouble(part_one),Double.parseDouble(part_two));
    }


    public String LoadJSON() {
        String json;
        File fileDir = context.getFilesDir();
        File file = new File(fileDir, filename);
        if (file.exists()) {
            FileInputStream fileInputStream;
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

            return json;
        } else
        {
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write("{\"locations\":[{\"name\":\"Edinburgh\",\"countryCode\":\"UK\",\"position\":\"55.953251,-3.188267\",\"rssKey\":\"2650225\"},{\"name\":\"Irvine\",\"countryCode\":\"UK\",\"position\":\"55.6115669,-4.6696364\",\"rssKey\":\"2646032\"},{\"name\":\"Cardiff\",\"countryCode\":\"UK\",\"position\":\"51.481583,-3.17909\",\"rssKey\":\"2653822\"},{\"name\":\"Edinburgh\",\"countryCode\":\"UK\",\"position\":\"55.953251,-3.188267\",\"rssKey\":\"2650225\"},{\"name\":\"Inverness\",\"countryCode\":\"UK\",\"position\":\"57.477772,-4.224721\",\"rssKey\":\"2646088\"},{\"name\":\"Irvine\",\"countryCode\":\"UK\",\"position\":\"55.6115669,-4.6696364\",\"rssKey\":\"2646032\"}]}");
                fileWriter.flush();
                fileWriter.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }






}
