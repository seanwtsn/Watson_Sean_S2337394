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


    public static ThreeDayLargeFragment newInstance() {
        return new ThreeDayLargeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_three_day_large, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView[][] textViews = new TextView[3][];

        //TODO: Fix the text views and some how make it speedy and easy to access.
        TextView temperatureOne;
        TextView windSpeedOne;
        TextView windDirOne;
        TextView uvOne;


        mViewModel = new ViewModelProvider(this).get(ThreeDayLargeViewModel.class);

        // TODO: Use the ViewModel
    }

}