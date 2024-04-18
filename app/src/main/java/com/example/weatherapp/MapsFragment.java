package com.example.weatherapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.weatherapp.data.LocationRSS;
import com.example.weatherapp.ui.viewmodels.MapsViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

public class MapsFragment extends Fragment {


    private MapsViewModel viewModel;
    private ArrayList<LocationRSS> locations;
    private GoogleMap map;

    public MapsFragment()
    {

    }

    public static MapsFragment newInstance()
    {
        Bundle bundle = new Bundle();
        MapsFragment mapsFragment = new MapsFragment();
        mapsFragment.setArguments(bundle);

        return mapsFragment;

    }
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap)
        {
            map = googleMap;
            map.getUiSettings().setZoomControlsEnabled(true);


            /*
            for (int i = 0; i < locations.size(); i++)
            {
                googleMap.addMarker(new MarkerOptions().position(locations.get(i).getPosition()));

            }

             */
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(MapsViewModel.class);

        Observer<ArrayList<LocationRSS>> observer = new Observer<ArrayList<LocationRSS>>() {
            @Override
            public void onChanged(ArrayList<LocationRSS> list) {

                locations = list;

            }
        };



        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }


}