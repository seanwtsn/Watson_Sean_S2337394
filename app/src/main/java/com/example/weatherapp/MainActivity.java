package com.example.weatherapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;

import com.example.weatherapp.data.LocationRSS;
import com.example.weatherapp.databinding.ActivityMainBinding;
import com.example.weatherapp.ui.fragments.ThreeDaySmallFragment;
import com.example.weatherapp.ui.viewmodels.MainViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private FragmentContainerView fragmentContainerView;


    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setContentView(R.layout.activity_main);
        fragmentContainerView = binding.fragmentContainerView;
        setFragmentContainerView(new ThreeDaySmallFragment());

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



    }
    private void setFragmentContainerView(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment).commit();
    }




}