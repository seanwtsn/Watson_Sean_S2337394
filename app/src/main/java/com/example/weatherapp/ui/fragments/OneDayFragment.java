package com.example.weatherapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.weatherapp.R;
import com.example.weatherapp.data.ExtendedWeather;
import com.example.weatherapp.ui.viewmodels.OneDayViewModel;

public class OneDayFragment extends Fragment {
    private ExtendedWeather currentWeather;
    private TextView statusView;
    public OneDayFragment() {
        // Required empty public constructor
    }

    public static OneDayFragment newInstance()
    {
        Bundle bundle = new Bundle();
        OneDayFragment oneDayFragment = new OneDayFragment();
        oneDayFragment.setArguments(bundle);

        return oneDayFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        OneDayViewModel oneDayViewModel = ViewModelProviders.of(this).get(OneDayViewModel.class);


        View view = inflater.inflate(R.layout.fragment_one_day, container, false);

        oneDayViewModel.getOneDayWeather().observe(getViewLifecycleOwner(), getData -> {

            statusView = view.findViewById(R.id.one_day_text_status);

            currentWeather = oneDayViewModel.getOneDayWeather().getValue();

            if(currentWeather == null)
            {
                errorWeather();
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
        sb.append("°C");
        return sb.toString();
    }
    private void updateWeather()
    {
        statusView.setText(buildStatusText());
    }
    private void errorWeather()
    {
        statusView.setText("Error! Cannot retrieve weather!");
    }

}