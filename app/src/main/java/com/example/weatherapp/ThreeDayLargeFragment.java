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

        TextView temp = view.findViewById(R.id.temperature_text_one);

        mViewModel.getData().observe(getViewLifecycleOwner(), getData ->
        {
            temp.setText(Float.toString((int)mViewModel.getData().getValue().getThreeDay().get(0).getHighTemperature()));

        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(ThreeDayLargeViewModel.class);

    }

}