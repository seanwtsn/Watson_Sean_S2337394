package com.example.weatherapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.weatherapp.data.LocationRSS;
import com.example.weatherapp.databinding.ActivityMainBinding;
import com.example.weatherapp.ui.fragments.MainFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

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

    public void onStart() {
        super.onStart();


        ArrayList<LocationRSS> locationRSS = new ArrayList<LocationRSS>() {
            {
                add(new LocationRSS("London", "UK", new LatLng(51.509865, -0.118092), "2643743"));
                add(new LocationRSS("Edinburgh", "UK", new LatLng(55.953251, -3.188267), "2650225"));
                add(new LocationRSS("Inverness", "UK", new LatLng(57.477772, -4.224721), "2646088"));
                add(new LocationRSS("Belfast", "UK", new LatLng(54.607868, -5.926437), "2655984"));
                add(new LocationRSS("Manchester", "UK", new LatLng(53.483959, -2.244644), "2643123"));
                add(new LocationRSS("Hull", "UK", new LatLng(53.767750, -0.335827), "2645425"));
                add(new LocationRSS("Cardiff", "UK", new LatLng(51.481583, -3.179090), "2653822"));
                add(new LocationRSS("Irvine", "UK", new LatLng(55.61156690, -4.66963640), "2646032"));
                add(new LocationRSS("Kilwinning", "UK", new LatLng(55.65333, -4.70666), "2645541"));

            }
        };
    }

}