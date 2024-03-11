package com.example.weatherapp;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.weatherapp.databinding.ActivityMainBinding;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final String urlSource = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643123";
    private String result;
    private ArrayList<ExtendedWeather> threeDayWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        startParse();
    }

    private void startParse(){
        new Thread(new Task(urlSource)).start();
    }

    private class Task implements Runnable{

        private final String url;

        public Task(String aurl){
            url = aurl;
        }

        @Override
        public void run() {
            URL aurl;
            URLConnection _url;
            BufferedReader in = null;
            String inputLine = "";

            Log.d("Testing", "run() called");

            try{
                aurl = new URL(url);
                _url = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader((_url.getInputStream())));
                while((inputLine = in.readLine()) != null){
                    result = result + inputLine;
                }
                in.close();
            } catch (IOException e) {

            }
            int i = result.indexOf(">");
            result = result.substring(i+1);
            i = result.indexOf(">");
            result = result.substring(i+1);

            Log.d("Testing", result);

            parseThreeDayXML();
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //TODO: Add Text display here from Runnable.
                }
            });
        }

        private void parseThreeDayXML(){
            ArrayList<BasicWeather> weather = new ArrayList<>(3);
            Log.d("Testing", "parseThreeDayXML run ");
            try
            {
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                parserFactory.setNamespaceAware(false);
                XmlPullParser pullParser = parserFactory.newPullParser();
                float maxTemp;
                float minTemp;
                String windDir = "";
                float windSpeed;
                String locName= "";
                String day = null;
                pullParser.setInput(new StringReader(result));
                int eventType = pullParser.getEventType();
                while(eventType != XmlPullParser.END_DOCUMENT)
                {
                    if(eventType == pullParser.START_TAG) {
                        //TODO: Implement all the tags in the 3 Day, and create separate conditions depending on what we need.
                        String temp;
                        switch (pullParser.getName())
                        {
                            case "title":
                                temp = pullParser.nextText();
                                Log.d("Testing", "parseThreeDayXML: title called");
                                if(temp != null && temp.contains("Minimum"))
                                {
                                    Log.d("Testing", "parseThreeDayXML: minimum condition called");

                                    String[] titleSplit = temp.split(":");

                                    if(!titleSplit[0].trim().equalsIgnoreCase("today"))
                                    {
                                        Log.d("Testing", "parseThreeDayXML: day condition called");

                                        day = titleSplit[0].trim().toUpperCase();
                                    }
                                }
                                else if (temp != null && temp.contains("BBC"))
                                {
                                    Log.d("Testing", "parseThreeDayXML: location condition called");

                                    String[] locSplit = temp.split("-");
                                    locName = locSplit[1].split(" ")[4];
                                }
                            break;

                            case "description":
                                temp = pullParser.nextText();
                                Log.d("Testing", "parseThreeDayXML: description called");
                                Log.d("Testing", temp);
                                if(temp != null && temp.contains("Maximum"))
                                {
                                    String[] strSplit = temp.split(",");
                                    maxTemp = valueFromString(strSplit[0]);
                                    minTemp = valueFromString(strSplit[1]);
                                    windDir = strSplit[2].split(":")[1].trim();
                                    windSpeed = Float.parseFloat(strSplit[3].split(":")[1].replace("mph", "").trim()); //I legitimately lost passion for life writing this
                                    Log.d("Testing", Float.toString(maxTemp));
                                    Log.d("Testing", Float.toString(minTemp));
                                    Log.d("Testing", windDir);
                                    Log.d("Testing", Float.toString(windSpeed));

                                    if(day == null || day.equalsIgnoreCase("today"))
                                    {
                                        weather.add(new BasicWeather(locName, null, maxTemp, minTemp, windSpeed, windDir, null));
                                    }
                                    else
                                    {
                                        weather.add(new BasicWeather(locName, null, maxTemp, minTemp, windSpeed, windDir, DayOfWeek.valueOf(day)));
                                    }

                                }
                            break;
                        }

                    }
                    eventType = pullParser.next();
                }

            }
            catch (XmlPullParserException ignore)
            {


            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }

    }

    private float valueFromString(String s){

        Pattern pattern = Pattern.compile("\\d+\\.?\\d*°C");
        Matcher matcher = pattern.matcher(s);
        if(matcher.find())
        {
          String temp = matcher.group().replace("°C", "");
          return Float.parseFloat(temp);
        }


        return Float.NaN;
    }

}