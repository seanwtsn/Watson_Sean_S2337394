package com.example.weatherapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.weatherapp.databinding.ActivityMainBinding;
import com.example.weatherapp.ui.fragments.MainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFragment(MainFragment.newInstance());

        com.example.weatherapp.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home)
                {
                    setFragment(MainFragment.newInstance());
                }
                else
                {
                    setFragment(MapsFragment.newInstance());
                }

                return true;
            }
        }
        );

    }

    private void setFragment(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.absolute_main_container, fragment).commit();

        getSupportFragmentManager().executePendingTransactions();
    }



}