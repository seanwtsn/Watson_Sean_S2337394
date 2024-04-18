package com.example.weatherapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.weatherapp.R;
import com.example.weatherapp.data.BasicWeather;
import com.example.weatherapp.helpers.WeatherIconHelper;
import com.example.weatherapp.ui.viewmodels.ThreeDaySmallViewModel;

import java.util.ArrayList;

public class ThreeDaySmallFragment extends Fragment {

    private ThreeDaySmallViewModel viewModel;

    public ThreeDaySmallFragment()
    {

    }

    public static ThreeDaySmallFragment newInstance()
    {
        Bundle bundle = new Bundle();
        ThreeDaySmallFragment threeDaySmallFragment = new ThreeDaySmallFragment();

        threeDaySmallFragment.setArguments(bundle);

        return threeDaySmallFragment;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ThreeDaySmallViewModel.class);


        ArrayList<TextView> temperatureView;
        ArrayList<TextView> dayView;
        ArrayList<ImageView> imageView;

        temperatureView = new ArrayList<>();

        dayView = new ArrayList<>();

        imageView = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                dayView.add(view.findViewById(R.id.text_today));
                dayView.add(view.findViewById(R.id.text_two));
                dayView.add(view.findViewById(R.id.text_three));


                temperatureView.add(view.findViewById(R.id.temperature_today));
                temperatureView.add(view.findViewById(R.id.temperature_two));
                temperatureView.add(view.findViewById(R.id.temperature_three));

                imageView.add(view.findViewById(R.id.image_icon_today));
                imageView.add(view.findViewById(R.id.image_icon_two));
                imageView.add(view.findViewById(R.id.image_icon_three));



                for (int i = 0; i < 3; i++)
                {
                    dayView.get(i).setVisibility(View.INVISIBLE);
                    temperatureView.get(i).setVisibility(View.INVISIBLE);
                    imageView.get(i).setVisibility(View.INVISIBLE);
                }
            }
        }).start();






        Observer<ArrayList<BasicWeather>> threeDayObserver = new Observer<ArrayList<BasicWeather>>() {
            @Override
            public void onChanged(ArrayList<BasicWeather> basicWeathers) {

                WeatherIconHelper weatherIconHelper = new WeatherIconHelper();

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        if(!basicWeathers.isEmpty())
                        {
                            view.findViewById(R.id.three_day_error).setVisibility(View.INVISIBLE);
                        }

                        for (int i = 0; i < 3; i++)
                        {
                            dayView.get(i).setVisibility(View.VISIBLE);
                            temperatureView.get(i).setVisibility(View.VISIBLE);
                            imageView.get(i).setVisibility(View.VISIBLE);
                        }
                    }
                }).start();



                for(int i = 0; i < basicWeathers.size(); i++)
                {
                    String s = basicWeathers.get(i).getDay().toString().substring(0,1).toUpperCase() + basicWeathers.get(i).getDay().toString().substring(1).split("DAY")[0].toLowerCase();
                    StringBuilder sb = new StringBuilder();
                    String h;

                    if(basicWeathers.get(i).getHighTemperature() != Float.NEGATIVE_INFINITY)
                    {
                        h = String.valueOf((int)basicWeathers.get(i).getHighTemperature());
                        sb.append(h);
                        sb.append("/");
                    }

                    sb.append(((int)basicWeathers.get(i).getLowTemperature()));
                    sb.append("°C");
                    temperatureView.get(i).setText(sb.toString());
                    imageView.get(i).setImageResource(weatherIconHelper.getWeatherIcon(basicWeathers.get(i).getConditions()));
                    dayView.get(i).setText(s);


                }


            }
        };


        viewModel.threeDaySmallLiveData().observe(getViewLifecycleOwner(), threeDayObserver);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
    {
        return inflater.inflate(R.layout.fragment_three_day_small, container, false);

    }

}
