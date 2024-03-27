package com.example.weatherapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class OneDayFragment extends Fragment {

    private MainViewModel mainModel;
    private TextView statusView;
    private ExtendedWeather currentWeather;
    public OneDayFragment() {
        // Required empty public constructor
    }

    public static OneDayFragment newInstance()
    {
        OneDayFragment fragment = new OneDayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mainModel = new ViewModelProvider(this).get(MainViewModel.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_one_day, container, false);
        statusView = view.findViewById(R.id.one_day_text_status);
        currentWeather = mainModel.getOneDayForecast();
        updateWeather();
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
        sb.append("High: ").append(currentWeather.getHighTemperature()).append(" Low: ").append(currentWeather.getLowTemperature());
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