package com.example.weatherapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class ThreeDayLargeFragment extends Fragment {

    private ThreeDayLargeViewModel mViewModel;
    private String rss;

    public ThreeDayLargeFragment(String rss)
    {
        this.rss = rss;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_three_day_large, container, false);

        ArrayList<TextView> temperatures;
        ArrayList<TextView> humidity;
        ArrayList<TextView> days;
        ArrayList<TextView> windSpeeds;
        ArrayList<TextView> windDirections;



        TextView temperatureOne = view.findViewById(R.id.temperature_text_one);
        TextView humidityOne = view.findViewById(R.id.humidity_text_one);
        TextView windSpeedOne = view.findViewById(R.id.wind_speed_text_one);
        TextView windDirOne = view.findViewById(R.id.wind_dir_text_one);
        TextView dayTextOne = view.findViewById(R.id.day_one_text);
        //TextView temperatureTwo = view.findViewById(R.id.temperature_text_two);
        //TextView humidityTwo = view.findViewById(R.id.humidity_text_two);
        //TextView windSpeedTwo = view.findViewById(R.id.wind_speed_text_two);
        //TextView windDirTwo = view.findViewById(R.id.wind_dir_text_two);




        mViewModel.getData().observe(getViewLifecycleOwner(), getData ->
        {
            String sb = Float.toString((int) mViewModel.getData().getValue().getThreeDayExtended().get(0).getHighTemperature()).replaceAll("\\.\\d+$", "°C") +
                    "/" +
                    Float.toString((int) mViewModel.getData().getValue().getThreeDayExtended().get(0).getLowTemperature()).replaceAll("\\.\\d+$", "°C");

            String dt = mViewModel.getData().getValue().getThreeDayExtended().get(0).getDay().toString().substring(0,1).toUpperCase() +
                    mViewModel.getData().getValue().getThreeDayExtended().get(0).getDay().toString().substring(1).toLowerCase();

            dayTextOne.setText(dt);
            temperatureOne.setText(sb);
            windDirOne.setText(mViewModel.getData().getValue().getWindDir(mViewModel.getData().getValue().getThreeDayExtended().get(0).getWindDirection()));
            windSpeedOne.setText(Float.toString((int) mViewModel.getData().getValue().getThreeDayExtended().get(0).getWindSpeed()).split("\\.")[0] + " mph");
            humidityOne.setText(Float.toString((int)mViewModel.getData().getValue().getThreeDayExtended().get(0).getHumidity()).replaceAll("\\.\\d+$", "%"));

        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(ThreeDayLargeViewModel.class);

    }

}