package com.example.weatherapp.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.weatherapp.data.ExtendedWeather;
import com.example.weatherapp.databinding.FragmentMainViewBinding;
import com.example.weatherapp.ui.viewmodels.MainFragmentViewModel;

public class MainFragment extends Fragment {

    private FragmentMainViewBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMainViewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView locationText = binding.mainLocationText;
        TextView temperatureText = binding.temperatureOneDayText;
        TextView conditionsText = binding.mainConditionsText;

        MainFragmentViewModel mainViewModel = new ViewModelProvider(this).get(MainFragmentViewModel.class);

        Observer<ExtendedWeather> condition = extendedWeather -> {
            Log.d("VMOC", extendedWeather.getConditions());
            Log.d("VMOC", extendedWeather.getLocationName());
            Log.d("VMOC", extendedWeather.getVisibility());

            StringBuilder sb = new StringBuilder();

            sb.append((int)extendedWeather.getCurrentTemperature()).append("°C");


            conditionsText.setText(extendedWeather.getConditions());
            temperatureText.setText(sb.toString());
            locationText.setText(extendedWeather.getLocationName());
        };

        mainViewModel.retrieveWeather().observe(getViewLifecycleOwner(), condition);

        return root;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}