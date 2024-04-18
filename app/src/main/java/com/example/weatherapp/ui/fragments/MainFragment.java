package com.example.weatherapp.ui.fragments;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.weatherapp.R;
import com.example.weatherapp.interfaces.OnLocationSelectedListener;
import com.example.weatherapp.models.MainModel;
import com.example.weatherapp.ui.viewmodels.MainFragmentViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.LatLng;

public class MainFragment extends Fragment
{
    private MainFragmentViewModel mainFragmentViewModel;
    private static final int PERMISSION_REQUEST = 123;
    private String rss;
    private LatLng currentPos;
    private final Object lock = new Object();

    public MainFragment()
    {

    }

    public static MainFragment newInstance()
    {
        Bundle bundle = new Bundle();
        MainFragment mainFragment = new MainFragment();
        mainFragment.setArguments(bundle);

        return mainFragment;

    }
    private final OnLocationSelectedListener onLocationSelectedListener = new OnLocationSelectedListener()
    {
        @Override
        public void onLocationSelected(String rss)
        {
            mainFragmentViewModel.getModel().getValue().doWeatherTask(rss);
        }
    };

    private FusedLocationProviderClient fusedLocationProviderClient;

    private final LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 4000)
            .setWaitForAccurateLocation(false).setMinUpdateIntervalMillis(LocationRequest.Builder.IMPLICIT_MIN_UPDATE_INTERVAL).setMaxUpdateDelayMillis(1).build();

    @Override
    public void onResume()
    {
        super.onResume();
    }

    private final LocationCallback locationCallback = new LocationCallback()
    {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            Location location = locationResult.getLocations().get(locationResult.getLocations().size() - 1);

            if (location != null)
            {
                currentPos = new LatLng(location.getLatitude(), location.getLongitude());
            }
            else
            {
                currentPos = new LatLng(51.509865,-0.118092);
                rss = "2643743";
            }

        }

        @Override
        public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
            super.onLocationAvailability(locationAvailability);

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_main_view, container, false);

        mainFragmentViewModel = new ViewModelProvider(this).get(MainFragmentViewModel.class);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        Observer<MainModel> observer = new Observer<MainModel>() {
            @Override
            public void onChanged(MainModel mainModel) {
                Log.d("MVVM", "OnChanged Called");

                if (currentPos != null) {
                    mainModel.getLocationRSS(getContext());
                    rss = mainModel.getRSSKeyFromLocation(currentPos);
                    mainModel.doWeatherTask(rss);
                    createUI();
                } else {
                    mainModel.getLocationRSS(getContext());
                    rss = mainModel.getRSSKeyFromLocation(new LatLng(51.509865, -0.118092));
                    mainModel.doWeatherTask(rss);
                    createUI();
                }
            }
        };

        mainFragmentViewModel.getModel().observe(getViewLifecycleOwner(), observer);

        return view;
    }

    private void createUI()
    {
        //we have to wait because the parser is too speedy for us.
        synchronized (lock)
        {
            try {
                lock.wait(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


            OneDayFragment oneDayFragment = OneDayFragment.newInstance();

            ThreeDaySmallFragment threeDaySmallFragment = ThreeDaySmallFragment.newInstance();

            FirstFragment firstFragment = FirstFragment.newInstance(onLocationSelectedListener);

            getChildFragmentManager().beginTransaction().replace(R.id.frag, firstFragment, "one").commit();

            getChildFragmentManager().beginTransaction().replace(R.id.frag_two, threeDaySmallFragment, "two").commit();

            getChildFragmentManager().beginTransaction().replace(R.id.frag_three, oneDayFragment, "three").commit();

            getChildFragmentManager().executePendingTransactions();


        }
    }

    public void onPause() {
        super.onPause();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }


}
