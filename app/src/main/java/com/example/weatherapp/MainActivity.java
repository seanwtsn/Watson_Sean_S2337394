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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String urlSource = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643123";
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

        private String url;

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
            BasicWeather weather = null;
            Log.d("Testing", "parseThreeDayXML run ");
            try
            {
                Log.d("Testing", "Try Statement");
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                parserFactory.setNamespaceAware(false);
                XmlPullParser pullParser = parserFactory.newPullParser();


                pullParser.setInput(new StringReader(result));
                int eventType = pullParser.getEventType();
                while(eventType != XmlPullParser.END_DOCUMENT)
                {

                    float maxTemp;
                    float minTemp;
                    String windDir;
                    String windSpeed;
                    String locName;
                    if(eventType == pullParser.START_TAG) {

                        //TODO: Implement all the tags in the 3 Day, and create separate conditions depending on what we need.

                        switch (pullParser.getName())
                        {
                            case "title":
                            break;
                            case "description":
                                String temp = pullParser.nextText();

                                if(temp != null && temp.contains("Maximum"))
                                {
                                    String[] strSplit = temp.split(",");
                                    for(int i = 0; i < strSplit.length; i++){
                                        Log.d("Parser", strSplit[i]);
                                    }
                                }
                            break;
                        }




                        if (pullParser.getName().equalsIgnoreCase("description"))
                        {
                            String temp = pullParser.nextText();

                            if(temp != null && temp.contains("Maximum"))
                            {
                                String[] strSplit = temp.split(",");
                                for(int i = 0; i < strSplit.length; i++){
                                    Log.d("Parser", strSplit[i]);
                                }
                            }
                        }

                                /*
                                maxTemp = Float.parseFloat(strSplit[0].split(":")[1]);
                                minTemp = Float.parseFloat(strSplit[1].split(":")[1]);
                                windDir = strSplit[2].split(":")[1];
                                windSpeed = strSplit[3].split(":")[1];
                                Log.d("Testing", Float.toString(maxTemp));
                                Log.d("Testing", Float.toString(minTemp));
                                Log.d("Testing", windDir);
                                Log.d("Testing", windSpeed);
                                */
                    }
                    eventType = pullParser.next();
                }

            }
            catch (XmlPullParserException ignore){


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }


}