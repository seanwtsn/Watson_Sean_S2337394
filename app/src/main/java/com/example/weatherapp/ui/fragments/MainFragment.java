package com.example.weatherapp.ui.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.weatherapp.data.ExtendedWeather;
import com.example.weatherapp.databinding.FragmentMainViewBinding;
import com.example.weatherapp.helpers.WeatherIconHelper;
import com.example.weatherapp.interfaces.OnLocationSelectedListener;
import com.example.weatherapp.ui.viewmodels.MainFragmentViewModel;

public class MainFragment extends Fragment {

    private FragmentMainViewBinding binding;
    private OnLocationSelectedListener onLocationSelectedListener;
    public MainFragment()
    {

    }
    public static MainFragment newInstance(OnLocationSelectedListener onLocationSelectedListener)
    {
        MainFragment mainFragment = new MainFragment();
        mainFragment.onLocationSelectedListener = onLocationSelectedListener;
        Bundle bundle = new Bundle();
        mainFragment.setArguments(bundle);
        return mainFragment;

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMainViewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button locationText = binding.mainLocationText;
        TextView temperatureText = binding.temperatureOneDayText;
        TextView conditionsText = binding.mainConditionsText;
        ImageView imageCondition = binding.conditionsImage;
        FragmentContainerView fragmentContainerView = binding.locationsFragment;
        CardView cardView = binding.locationsCard;



        fragmentContainerView.setVisibility(View.GONE);

        getChildFragmentManager().beginTransaction().replace(fragmentContainerView.getId(), WeatherFragment.newInstance(onLocationSelectedListener)).commit();

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

            WeatherIconHelper weatherIconHelper = new WeatherIconHelper();

            sb.append((int)extendedWeather.getCurrentTemperature()).append("°C");

            conditionsText.setText(extendedWeather.getConditions());
            temperatureText.setText(sb.toString());
            locationText.setText(extendedWeather.getLocationName().replace(",",""));
            imageCondition.setImageResource(weatherIconHelper.getWeatherIcon(extendedWeather.getConditions()));
        };

        mainViewModel.retrieveWeather().observe(getViewLifecycleOwner(), condition);



        return root;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration configuration)
    {
        super.onConfigurationChanged(configuration);



    }






    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}