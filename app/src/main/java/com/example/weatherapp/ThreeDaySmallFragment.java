package com.example.weatherapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class ThreeDaySmallFragment extends Fragment {

    private ArrayList<TextView> dayView;
    private ArrayList<TextView> temperatureView;
    private MainViewModel mainViewModel;

    private ArrayList<BasicWeather> threeDay;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        threeDay = mainViewModel.getThreeDaySimple();

        for (int i = 0; i < threeDay.size(); i++)
        {
            Log.d("3D", threeDay.get(i).getDay().toString());
            Log.d("3D", Float.toString(threeDay.get(i).getHighTemperature()));
            Log.d("3D", Float.toString(threeDay.get(i).getLowTemperature()));
        }

        temperatureView = new ArrayList<>();

        dayView = new ArrayList<>();

        dayView.add(view.findViewById(R.id.text_today));
        dayView.add(view.findViewById(R.id.text_two));
        dayView.add(view.findViewById(R.id.text_three));

        for(int i = 0; i < threeDay.size(); i++)
        {
            String s = threeDay.get(i).getDay().toString().substring(0,1).toUpperCase() + threeDay.get(i).getDay().toString().substring(1).split("DAY")[0].toLowerCase();

            dayView.get(i).setText(s);
        }

        temperatureView.add(view.findViewById(R.id.temperature_today));
        temperatureView.add(view.findViewById(R.id.temperature_two));
        temperatureView.add(view.findViewById(R.id.temperature_three));

        for (int i = 0; i < temperatureView.size(); i++)
        {
            StringBuilder sb = new StringBuilder();
            sb.append(((int)threeDay.get(i).getHighTemperature()));
            sb.append("°C");
            sb.append("/");
            sb.append(((int)threeDay.get(i).getLowTemperature()));
            sb.append("°C");

            temperatureView.get(i).setText(sb.toString());

        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
    {
        View view = inflater.inflate(R.layout.fragment_three_day_small, container, false);

        return view;
    }

}
