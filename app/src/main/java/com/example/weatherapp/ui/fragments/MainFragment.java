package com.example.weatherapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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

        MainFragmentViewModel mainViewModel = ViewModelProviders.of(this).get(MainFragmentViewModel.class);

        Observer<ExtendedWeather> condition = new Observer<ExtendedWeather>() {
            @Override
            public void onChanged(ExtendedWeather extendedWeather) {
                conditionsText.setText(extendedWeather.getConditions());
                temperatureText.setText(Float.toString(extendedWeather.getCurrentTemperature()));
                locationText.setText(extendedWeather.getLocationName());
            }
        };

        mainViewModel.retrieveWeather().observe(requireActivity(), condition);

        return root;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}