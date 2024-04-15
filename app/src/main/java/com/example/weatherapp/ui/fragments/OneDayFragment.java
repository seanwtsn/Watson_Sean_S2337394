package com.example.weatherapp.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.weatherapp.R;
import com.example.weatherapp.data.ExtendedWeather;
import com.example.weatherapp.ui.viewmodels.MainViewModel;

public class OneDayFragment extends Fragment {

    private MainViewModel mainModel;
    private ExtendedWeather currentWeather;
    private TextView statusView;
    public OneDayFragment() {
        // Required empty public constructor
    }@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_one_day, container, false);
        mainModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainModel.getData().observe(getViewLifecycleOwner(), getData -> {

            statusView = view.findViewById(R.id.one_day_text_status);


            currentWeather = mainModel.getOneDayForecast();

            if(currentWeather == null)
            {
                
            }
            else
            {
                updateWeather();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    private String buildStatusText()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("High: ");
        sb.append((int) currentWeather.getHighTemperature());
        sb.append("°C");
        sb.append((", Low: "));
        sb.append((int) currentWeather.getLowTemperature());
        sb.append("°C,");
        return sb.toString();
    }
    private void updateWeather()
    {
        if(currentWeather != null)
        {
            statusView.setText(buildStatusText());
        }
        else
        {
            Log.d("Testing", "Current Weather is Null");





        }
    }

}