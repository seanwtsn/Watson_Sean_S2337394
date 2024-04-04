package com.example.weatherapp.ui.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.ui.viewmodels.MainViewModel;
import com.example.weatherapp.R;
import com.example.weatherapp.WeatherRecyclerViewAdapter;

public class WeatherFragment extends Fragment {

    private MainViewModel viewModel;
    public WeatherFragment()
    {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_item_list, container, false);

        viewModel.getData().observe(getViewLifecycleOwner(), mainModel -> {

            if (view instanceof RecyclerView) {
                Context context = view.getContext();
                RecyclerView recyclerView = (RecyclerView) view;
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(new WeatherRecyclerViewAdapter(viewModel.getLocationFromName(), viewModel));
            }
        });

        // Set the adapter

        return view;
    }
}