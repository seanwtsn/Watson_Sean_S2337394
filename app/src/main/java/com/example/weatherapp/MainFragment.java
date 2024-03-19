package com.example.weatherapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.weatherapp.databinding.FragmentMainViewBinding;

public class MainFragment extends Fragment {

    private FragmentMainViewBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MainViewModel mainViewModel =
                new ViewModelProvider(this).get(MainViewModel.class);



        binding = FragmentMainViewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();




        return root;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}