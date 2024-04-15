package com.example.weatherapp;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.weatherapp.data.LocationRSS;
import com.example.weatherapp.databinding.ActivityMainBinding;
import com.example.weatherapp.helpers.ReturnClosestLocationHelper;
import com.example.weatherapp.models.MainModel;
import com.example.weatherapp.ui.fragments.MainFragment;
import com.example.weatherapp.ui.fragments.OneDayFragment;
import com.example.weatherapp.ui.fragments.ThreeDaySmallFragment;
import com.example.weatherapp.ui.viewmodels.MainActivityViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainActivityViewModel viewModel;
    private String rss;
    private LatLng currentPos;

    private final MainModel mm = MainModel.getModelInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mm.doWeatherTask("2648579");

        setContentView(R.layout.activity_main);

        ReturnClosestLocationHelper returnClosestLocationHelper = new ReturnClosestLocationHelper();

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2500)
                .setWaitForAccurateLocation(false).setMinUpdateIntervalMillis(LocationRequest.Builder.IMPLICIT_MIN_UPDATE_INTERVAL).setMaxUpdateDelayMillis(1).build();

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null)
                {
                    currentPos = new LatLng(location.getLatitude(), location.getLongitude());

                }
                else
                {
                    //defaults to London
                    currentPos = new LatLng(51.509865, -0.118092);
                }


            }
        });

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button _button = binding.TEST;

        _button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Log.d("MAVM", "onClick");
            }
        });



    }
    public void onStart() {
        super.onStart();
        ArrayList<LocationRSS> locationRSS = new ArrayList<LocationRSS>()
        {
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

    }
    private void setFragmentContainerView(Fragment fragment)
    {

    }




}