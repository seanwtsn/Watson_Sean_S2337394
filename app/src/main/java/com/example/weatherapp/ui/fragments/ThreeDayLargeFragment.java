package com.example.weatherapp.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.weatherapp.R;
import com.example.weatherapp.ui.viewmodels.ThreeDayLargeViewModel;

import java.util.ArrayList;

public class ThreeDayLargeFragment extends Fragment {

    private ThreeDayLargeViewModel mViewModel;
    public ThreeDayLargeFragment(String rss)
    {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_three_day_large, container, false);

        ArrayList<ConstraintLayout> constraintLayouts = new ArrayList<>();

        constraintLayouts.add(view.findViewById(R.id.first_item));
        constraintLayouts.add(view.findViewById(R.id.second_item));
        constraintLayouts.add(view.findViewById(R.id.third_item));

        mViewModel.getData().observe(getViewLifecycleOwner(), getData ->
        {
            Log.d("MVVM", Integer.toString(mViewModel.getData().getValue().getThreeDayExtended().size()));


            for(int i = 0; i < constraintLayouts.size(); i++)
            {
                String sb = Float.toString((int) mViewModel.getData().getValue().getThreeDayExtended().get(i).getHighTemperature()).replaceAll("\\.\\d+$", "°C") +
                        "/" +
                        Float.toString((int) mViewModel.getData().getValue().getThreeDayExtended().get(i).getLowTemperature()).replaceAll("\\.\\d+$", "°C");

                String dt = mViewModel.getData().getValue().getThreeDayExtended().get(i).getDay().toString().substring(0,1).toUpperCase() +
                        mViewModel.getData().getValue().getThreeDayExtended().get(i).getDay().toString().substring(1).toLowerCase();

                TextView dayText = constraintLayouts.get(i).findViewById(R.id.day_one_text);
                        dayText.setText(dt);

                        TextView temperature = constraintLayouts.get(i).findViewById(R.id.temperature_text_one);
                temperature.setText(sb);

                TextView windDir = constraintLayouts.get(i).findViewById(R.id.wind_dir_text_one);
                windDir.setText(mViewModel.getData().getValue().getWindDir(mViewModel.getData().getValue().getThreeDayExtended().get(i).getWindDirection()));

                TextView windSpeed = constraintLayouts.get(i).findViewById(R.id.wind_speed_text_one);
                windSpeed.setText(String.format("%s mph", Float.toString((int) mViewModel.getData().getValue().getThreeDayExtended().get(i).getWindSpeed()).split("\\.")[0]));

                TextView humidity = constraintLayouts.get(i).findViewById(R.id.humidity_text_one);
                humidity.setText(Float.toString((int)mViewModel.getData().getValue().getThreeDayExtended().get(i).getHumidity()).replaceAll("\\.\\d+$", "%"));

            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(ThreeDayLargeViewModel.class);
        mViewModel.getData().getValue().doWeatherTask();


    }

}