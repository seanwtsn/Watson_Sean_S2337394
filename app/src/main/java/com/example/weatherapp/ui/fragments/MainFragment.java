package com.example.weatherapp.ui.fragments;

import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.weatherapp.data.ExtendedWeather;
import com.example.weatherapp.databinding.FragmentMainViewBinding;
import com.example.weatherapp.interfaces.OnLocationSelectedListener;
import com.example.weatherapp.ui.viewmodels.MainFragmentViewModel;

public class MainFragment extends Fragment {

    private FragmentMainViewBinding binding;
    private final WeatherFragment weatherFragment;

    public MainFragment(OnLocationSelectedListener onLocationSelectedListener)
    {
        weatherFragment = new WeatherFragment(onLocationSelectedListener);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMainViewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button locationText = binding.mainLocationText;
        TextView temperatureText = binding.temperatureOneDayText;
        TextView conditionsText = binding.mainConditionsText;
        FragmentContainerView fragmentContainerView = binding.locationsFragment;
        CardView cardView = binding.locationsCard;



        fragmentContainerView.setVisibility(View.GONE);

        getChildFragmentManager().beginTransaction().replace(fragmentContainerView.getId(), weatherFragment).commit();

        getChildFragmentManager().executePendingTransactions();
        locationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(fragmentContainerView.getVisibility() == View.GONE)
                {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    fragmentContainerView.setVisibility(View.VISIBLE);

                }
                else
                {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    fragmentContainerView.setVisibility(View.GONE);
                }
            }
        });

        MainFragmentViewModel mainViewModel = new ViewModelProvider(this).get(MainFragmentViewModel.class);

        Observer<ExtendedWeather> condition = extendedWeather -> {
            StringBuilder sb = new StringBuilder();

            sb.append((int)extendedWeather.getCurrentTemperature()).append("°C");

            conditionsText.setText(extendedWeather.getConditions());
            temperatureText.setText(sb.toString());
            locationText.setText(extendedWeather.getLocationName().replace(",",""));
        };

        mainViewModel.retrieveWeather().observe(getViewLifecycleOwner(), condition);



        return root;
    }


    //TODO: FIGURE OUT THE DATA OBSERVING THING, ONLY BEING TRIGGER AT INACTIVE



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}