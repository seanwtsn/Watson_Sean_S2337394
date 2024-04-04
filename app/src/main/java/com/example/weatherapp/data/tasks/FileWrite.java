package com.example.weatherapp.data.tasks;

import android.content.Context;
import android.util.Log;

import com.example.weatherapp.data.LocationRSS;
import com.google.android.gms.maps.model.LatLng;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileWrite implements Runnable {
    private static final String filename = "locations.json";
    private LocationRSS locationRSS;
    private Context context;

    public FileWrite(LocationRSS locationRSS, Context context) {
        this.context = context;
        this.locationRSS = locationRSS;
    }

    @Override
    public void run() {
        saveLocation(locationRSS);
    }

    private void saveLocation(LocationRSS locationRSS)
    {
        File fileDir = context.getFilesDir();
        if (!context.getDir(filename, context.MODE_PRIVATE).exists())
        {
            fileDir.mkdir();
        }
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("name", locationRSS.getName());
            jsonObject.put("countryCode", locationRSS.getCountryCode());
            jsonObject.put("position", LatLngToString(locationRSS.getPosition()));
            jsonObject.put("rssKey", locationRSS.getRssKey());
        }
        catch (JSONException e)
        {
            throw new RuntimeException(e);
        }

        if(LoadJSON() != null)
        {
            try
            {
                Log.i("Save Testing", "File Exists, so we do our thing");

                String existingFile = LoadJSON();
                FileOutputStream outputStreamWriter = context.openFileOutput(filename, Context.MODE_PRIVATE);

                JSONObject obj = new JSONObject(existingFile);
                JSONArray jsonA = obj.getJSONArray("locations");
                jsonA.put(jsonObject);


                JSONObject jo = new JSONObject();
                jo.put("locations", jsonA);

                outputStreamWriter.write(jo.toString().getBytes());
                outputStreamWriter.flush();
                outputStreamWriter.close();
            }
            catch (JSONException e)
            {
                Log.e("DED", "saveAlarm: IOE ", e);
                throw new RuntimeException(e);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        else
        {
            FileOutputStream outputStreamWriter = null;
            try {
                outputStreamWriter = context.openFileOutput(filename, Context.MODE_PRIVATE);
                JSONArray ar = new JSONArray();
                JSONObject j = new JSONObject();
                ar.put(jsonObject);
                j.put("locations", ar);
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







    private static String LatLngToString(LatLng latLng)
    {
        double lat = latLng.latitude;
        double lng = latLng.longitude;

        StringBuilder sb = new StringBuilder();
        return String.valueOf(sb.append(Double.toString(lat)).append(",").append(Double.toString(lng)));
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
}
