package com.example.weatherapp.ui.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.WeatherRecyclerViewAdapter;
import com.example.weatherapp.interfaces.OnLocationSelectedListener;
import com.example.weatherapp.ui.viewmodels.WeatherViewModel;

public class WeatherFragment extends Fragment {

    private WeatherViewModel viewModel;
    private OnLocationSelectedListener onLocationSelectedListener;
    public static WeatherFragment newInstance(OnLocationSelectedListener onLocationSelectedListener)
    {
        Bundle bundle = new Bundle();
        WeatherFragment weatherFragment = new WeatherFragment();
        weatherFragment.onLocationSelectedListener = onLocationSelectedListener;
        weatherFragment.setArguments(bundle);

        return weatherFragment;
    }
    public WeatherFragment()
    {

    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        View view = inflater.inflate(R.layout.fragment_weather_item_list, container, false);

        viewModel.getList().observe(getViewLifecycleOwner(), mainModel -> {

            if (view instanceof RecyclerView) {
                Context context = view.getContext();
                RecyclerView recyclerView = (RecyclerView) view;
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(new WeatherRecyclerViewAdapter(onLocationSelectedListener, viewModel.getList().getValue(), viewModel));
            }
        });

        // Set the adapter

        return view;
    }
}