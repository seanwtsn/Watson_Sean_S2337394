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

import com.example.weatherapp.R;
import com.example.weatherapp.data.ExtendedWeather;
import com.example.weatherapp.helpers.WeatherIconHelper;
import com.example.weatherapp.interfaces.OnLocationSelectedListener;
import com.example.weatherapp.ui.viewmodels.FirstFragmentViewModel;

public class FirstFragment extends Fragment {
    private OnLocationSelectedListener onLocationSelectedListener;
    public FirstFragment()
    {

    }
    public static FirstFragment newInstance(OnLocationSelectedListener onLocationSelectedListener)
    {
        FirstFragment firstFragment = new FirstFragment();
        firstFragment.onLocationSelectedListener = onLocationSelectedListener;
        Bundle bundle = new Bundle();
        firstFragment.setArguments(bundle);
        return firstFragment;

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_first, container, false);


        Button locationText = root.findViewById(R.id.main_location_text);
        TextView temperatureText = root.findViewById(R.id.temperature_one_day_text);
        TextView conditionsText = root.findViewById(R.id.main_conditions_text);
        ImageView imageCondition = root.findViewById(R.id.conditions_image);
        FragmentContainerView fragmentContainerView = root.findViewById(R.id.locationsFragment);
        CardView cardView = root.findViewById(R.id.locationsCard);



        fragmentContainerView.setVisibility(View.GONE);

        getChildFragmentManager().beginTransaction().replace(fragmentContainerView.getId(), LocationsFragment.newInstance(onLocationSelectedListener)).commit();

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

        FirstFragmentViewModel mainViewModel = new ViewModelProvider(this).get(FirstFragmentViewModel.class);

        Observer<ExtendedWeather> condition = extendedWeather -> {
            StringBuilder sb = new StringBuilder();



            sb.append((int)extendedWeather.getCurrentTemperature()).append("°C");

            conditionsText.setText(extendedWeather.getConditions());
            temperatureText.setText(sb.toString());
            locationText.setText(extendedWeather.getLocationName().replace(",",""));
            imageCondition.setImageResource(WeatherIconHelper.getWeatherIcon(extendedWeather.getConditions()));
        };

        mainViewModel.retrieveWeather().observe(getViewLifecycleOwner(), condition);



        return root;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration configuration)
    {
        super.onConfigurationChanged(configuration);



    }

}