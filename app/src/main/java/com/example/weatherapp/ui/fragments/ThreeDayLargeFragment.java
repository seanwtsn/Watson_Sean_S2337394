package com.example.weatherapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.weatherapp.R;
import com.example.weatherapp.data.ExtendedWeather;
import com.example.weatherapp.helpers.GetWindDirectionHelper;
import com.example.weatherapp.helpers.WeatherIconHelper;
import com.example.weatherapp.ui.viewmodels.ThreeDayLargeViewModel;

import java.util.ArrayList;

public class ThreeDayLargeFragment extends Fragment {


    public ThreeDayLargeFragment()
    {

    }
    public static ThreeDayLargeFragment newInstance()
    {
        Bundle bundle = new Bundle();
        ThreeDayLargeFragment threeDayLargeFragment = new ThreeDayLargeFragment();
        threeDayLargeFragment.setArguments(bundle);

        return threeDayLargeFragment;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ThreeDayLargeViewModel mViewModel;
        mViewModel = new ViewModelProvider(this).get(ThreeDayLargeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_three_day_large, container, false);
        ArrayList<ConstraintLayout> constraintLayouts = new ArrayList<>();
        constraintLayouts.add(view.findViewById(R.id.first_item));
        constraintLayouts.add(view.findViewById(R.id.second_item));
        constraintLayouts.add(view.findViewById(R.id.third_item));

        ArrayList<String> dt = new ArrayList<>();
        ArrayList<String>  sb =  new ArrayList<>();
        ArrayList<TextView> humidity =  new ArrayList<>();
        ArrayList<TextView> windSpeed =  new ArrayList<>();
        ArrayList<TextView> temperature =  new ArrayList<>();
        ArrayList<TextView> dayText = new ArrayList<>();
        ArrayList<TextView> windDir = new ArrayList<>();
        ArrayList<ImageView> conditionImage = new ArrayList<>();

        Observer<ArrayList<ExtendedWeather>> observer = new Observer<ArrayList<ExtendedWeather>>() {
            @Override
            public void onChanged(ArrayList<ExtendedWeather> getData) {


                for (int i = 0; i < constraintLayouts.size(); i++) {

                    if(getData.get(i).getHighTemperature() == Float.NEGATIVE_INFINITY)
                    {
                        sb.add("" + Float.toString((int) getData.get(i).getLowTemperature()).replaceAll("\\.\\d+$", "°C"));
                    }
                    else
                    {
                        sb.add(Float.toString((int) getData.get(i).getHighTemperature()).replaceAll("\\.\\d+$", "°C") +
                                "/" +
                                Float.toString((int) getData.get(i).getLowTemperature()).replaceAll("\\.\\d+$", "°C"));
                    }



                    dt.add(getData.get(i).getDay().toString().substring(0, 1).toUpperCase() +
                            getData.get(i).getDay().toString().substring(1).toLowerCase());
                    dayText.add(constraintLayouts.get(i).findViewById(R.id.day_one_text));
                    windDir.add(constraintLayouts.get(i).findViewById(R.id.wind_dir_text_one));
                    temperature.add(constraintLayouts.get(i).findViewById(R.id.temperature_text_one));
                    windSpeed.add(constraintLayouts.get(i).findViewById(R.id.wind_speed_text_one));
                    humidity.add(constraintLayouts.get(i).findViewById(R.id.humidity_text_one));
                    conditionImage.add(constraintLayouts.get(i).findViewById(R.id.condition_icon_one));


                }

                for (int j = 0; j < dt.size(); j++) {
                    dayText.get(j).setText(dt.get(j));
                    temperature.get(j).setText(sb.get(j));
                    windDir.get(j).setText(GetWindDirectionHelper.getWindDir(getData.get(j).getWindDirection()));
                    windSpeed.get(j).setText(String.format("%s mph", Float.toString((int) getData.get(j).getWindSpeed()).split("\\.")[0]));
                    humidity.get(j).setText(Float.toString((int) getData.get(j).getHumidity()).replaceAll("\\.\\d+$", "%"));
                    conditionImage.get(j).setImageResource(WeatherIconHelper.getWeatherIcon(getData.get(j).getConditions()));
                }

            }

        };

        mViewModel.getData().observe(getViewLifecycleOwner(), observer);





        return view;
    }



}