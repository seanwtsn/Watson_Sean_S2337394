package com.example.weatherapp.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.weatherapp.R;
import com.example.weatherapp.data.ExtendedWeather;

public class OneDayGridFragment extends Fragment {

    private OneDayGridViewModel mViewModel;

    public OneDayGridFragment()
    {

    }
    public static OneDayGridFragment newInstance()
    {
        Bundle bundle = new Bundle();
        OneDayGridFragment oneDayGridFragment = new OneDayGridFragment();
        oneDayGridFragment.setArguments(bundle);

        return oneDayGridFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(OneDayGridViewModel.class);

        View view = inflater.inflate(R.layout.fragment_one_day_grid, container, false);
        Observer<ExtendedWeather> weatherObserver = new Observer<ExtendedWeather>() {
            @Override
            public void onChanged(ExtendedWeather weather)
            {
                new Thread(new Runnable() {
                    @Override
                    public void run()
                    {
                        TextView visibilityText = view.findViewById(R.id.visibility_text);
                        TextView pressureText = view.findViewById(R.id.pressure_text);
                        TextView humidityText = view.findViewById(R.id.humidity_text);
                        TextView uvrisk = view.findViewById(R.id.uvrisk_text);
                        TextView sunriseText = view.findViewById(R.id.sunrise_text);
                        TextView sunsetText = view.findViewById(R.id.sunset_text);

                        String vt = weather.getVisibility();
                        String pt = String.valueOf((int)weather.getPressure()) + "mb";
                        String ht = String.valueOf((int)weather.getHumidity()) + "%";
                        String uvt = String.valueOf(weather.getUvRisk());
                        String srt = String.valueOf(weather.getSunrise()) + "AM";
                        String sst = String.valueOf(weather.getSunset()) + "PM";



                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run()
                            {
                                visibilityText.setText(vt);
                                pressureText.setText(pt);
                                humidityText.setText(ht);
                                uvrisk.setText(uvt);
                                sunriseText.setText(srt);
                                sunsetText.setText(sst);
                            }
                        });

                    }
                }).start();
            }
        };

        mViewModel.getExtended().observe(getViewLifecycleOwner(), weatherObserver);


        return view;



    }



}