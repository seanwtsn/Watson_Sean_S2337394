package com.example.weatherapp;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.weatherapp.data.LocationRSS;
import com.example.weatherapp.databinding.ActivityMainBinding;
import com.example.weatherapp.models.MainModel;
import com.example.weatherapp.ui.fragments.MainFragment;
import com.example.weatherapp.ui.fragments.OneDayFragment;
import com.example.weatherapp.ui.fragments.ThreeDaySmallFragment;
import com.example.weatherapp.ui.viewmodels.MainActivityViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST = 123;
    private MainActivityViewModel viewModel;
    private String rss;
    private LatLng currentPos;

    private final MainModel mm = MainModel.getModelInstance();

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            Location location = locationResult.getLocations().get(locationResult.getLocations().size() - 1);

            if (location != null) {
                //currentPos = new LatLng(location.getLatitude(), location.getLongitude());
                currentPos = new LatLng(51.509865, -0.118092);
                rss = mm.getRSSKeyFromLocation(currentPos);
                mm.doWeatherTask(rss);

                //mm.doWeatherTask(mm.getRSSKeyFromLocation(currentPos));


            } else {
                //defaults to London
                rss = "2643743";
                mm.doWeatherTask(rss);
                //mm.doWeatherTask(mm.getRSSKeyFromLocation(currentPos));


            }

        }

        @Override
        public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
            super.onLocationAvailability(locationAvailability);

        }
    };

    private FusedLocationProviderClient fusedLocationProviderClient;

    private final LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 4000)
            .setWaitForAccurateLocation(false).setMinUpdateIntervalMillis(LocationRequest.Builder.IMPLICIT_MIN_UPDATE_INTERVAL).setMaxUpdateDelayMillis(1).build();

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mm.getLocationRSS(getApplicationContext());

        com.example.weatherapp.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST);
            return;
        }


    }

    public void startingLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                rss = mm.getRSSKeyFromLocation(new LatLng(location.getLatitude(), location.getLongitude()));
                mm.doWeatherTask(rss);
            }
        });
    }

    public void onStart() {
        super.onStart();


        ArrayList<LocationRSS> locationRSS = new ArrayList<LocationRSS>() {
            {
                add(new LocationRSS("London", "UK", new LatLng(51.509865, -0.118092), "2643743"));
                add(new LocationRSS("Edinburgh", "UK", new LatLng(55.953251, -3.188267), "2650225"));
                add(new LocationRSS("Inverness", "UK", new LatLng(57.477772, -4.224721), "2646088"));
                add(new LocationRSS("Belfast", "UK", new LatLng(54.607868, -5.926437), "2655984"));
                add(new LocationRSS("Manchester", "UK", new LatLng(53.483959, -2.244644), "2643123"));
                add(new LocationRSS("Hull", "UK", new LatLng(53.767750, -0.335827), "2645425"));
                add(new LocationRSS("Cardiff", "UK", new LatLng(51.481583, -3.179090), "2653822"));
                add(new LocationRSS("Irvine", "UK", new LatLng(55.61156690, -4.66963640), "2646032"));
                add(new LocationRSS("Kilwinning", "UK", new LatLng(55.65333, -4.70666), "2645541"));

            }
        };


        OneDayFragment oneDayFragment = new OneDayFragment();

        ThreeDaySmallFragment threeDaySmallFragment = new ThreeDaySmallFragment();

        MainFragment mainFragment = new MainFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.frag, mainFragment, "one").commit();

        getSupportFragmentManager().beginTransaction().replace(R.id.frag_two, threeDaySmallFragment, "two").commit();

        getSupportFragmentManager().beginTransaction().replace(R.id.frag_three, oneDayFragment, "two").commit();

        getSupportFragmentManager().executePendingTransactions();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

        startingLocation();

    }





}