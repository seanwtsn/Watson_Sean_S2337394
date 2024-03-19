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

public class ThreeDaySmallFragment extends Fragment {

    private ArrayList<TextView> dayView;
    private MainViewModel mainViewModel;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);



        mainViewModel.getOneDaySimple();

        //dayView = new ArrayList<>();

        //dayView.add(view.findViewById(R.id.text_today));
        //dayView.add(view.findViewById(R.id.text_two));
        //dayView.add(view.findViewById(R.id.text_three));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
    {
        View view = inflater.inflate(R.layout.fragment_three_day_small, container, false);

        return view;
    }

}
