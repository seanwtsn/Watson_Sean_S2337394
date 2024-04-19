package com.example.weatherapp.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.weatherapp.R;
import com.example.weatherapp.interfaces.OnLocationSelectedListener;
import com.example.weatherapp.models.MainModel;
import com.example.weatherapp.ui.viewmodels.MainFragmentViewModel;
import com.google.android.gms.common.api.internal.ConnectionCallbacks;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainFragment extends Fragment implements ConnectionCallbacks
{
    private MainFragmentViewModel mainFragmentViewModel;
    private static final int PERMISSION_REQUEST = 123;
    private String rss;
    private LatLng currentPos;
    private final Object lock = new Object();



    public MainFragment()
    {

    }

    public static MainFragment newInstance()
    {
        Bundle bundle = new Bundle();
        MainFragment mainFragment = new MainFragment();
        mainFragment.setArguments(bundle);

        return mainFragment;

    }
    private final OnLocationSelectedListener onLocationSelectedListener = new OnLocationSelectedListener()
    {
        @Override
        public void onLocationSelected(String rss)
        {
            mainFragmentViewModel.getModel().getValue().doWeatherTask(rss);
        }
    };

    private FusedLocationProviderClient fusedLocationProviderClient;

    private final LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 4000)
            .setWaitForAccurateLocation(false).setMinUpdateIntervalMillis(LocationRequest.Builder.IMPLICIT_MIN_UPDATE_INTERVAL).setMaxUpdateDelayMillis(1).build();

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_main_view, container, false);

        mainFragmentViewModel = new ViewModelProvider(this).get(MainFragmentViewModel.class);

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);

        TextView refreshedTextView = view.findViewById(R.id.last_refresh_text);

        rss = "2648579";

        currentPos = new LatLng(55.858700, -4.295905);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                mainFragmentViewModel.getModel().getValue().doWeatherTask(rss);


                refreshedTextView.setText(buildRefreshText());


                swipeRefreshLayout.setRefreshing(false);
            }


        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        Observer<MainModel> observer = new Observer<MainModel>() {
            @Override
            public void onChanged(MainModel mainModel) {
                Log.d("MVVM", "OnChanged Called");

                if (currentPos != null) {
                    mainModel.getLocationRSS(getContext());
                    rss = mainModel.getRSSKeyFromLocation(currentPos);
                    refreshedTextView.setText(buildRefreshText());
                    mainModel.doWeatherTask(rss);
                    createUI();
                } else {
                    mainModel.getLocationRSS(getContext());
                    refreshedTextView.setText(buildRefreshText());
                    rss = mainModel.getRSSKeyFromLocation(new LatLng(51.509865, -0.118092));
                    mainModel.doWeatherTask(rss);
                    createUI();
                }
            }
        };

        mainFragmentViewModel.getModel().observe(getViewLifecycleOwner(), observer);



        return view;
    }

    private void createUI()
    {
        //we have to wait because the parser is too speedy for us.
        synchronized (lock)
        {
            try {
                lock.wait(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }



            OneDayFragment oneDayFragment = OneDayFragment.newInstance();

            ThreeDaySmallFragment threeDaySmallFragment = ThreeDaySmallFragment.newInstance();

            FirstFragment firstFragment = FirstFragment.newInstance(onLocationSelectedListener);

            OneDayGridFragment oneDayGridFragment = OneDayGridFragment.newInstance();

            ThreeDayLargeFragment threeDayLargeFragment = ThreeDayLargeFragment.newInstance();

            getChildFragmentManager().beginTransaction().replace(R.id.frag, firstFragment, "one").commit();

            getChildFragmentManager().beginTransaction().replace(R.id.frag_two, oneDayFragment, "two").commit();

            getChildFragmentManager().beginTransaction().replace(R.id.frag_three, threeDaySmallFragment, "three").commit();

            getChildFragmentManager().beginTransaction().replace(R.id.frag_grid, oneDayGridFragment, "four").commit();

            getChildFragmentManager().beginTransaction().replace(R.id.frag_five, threeDayLargeFragment, "five").commit();


            getChildFragmentManager().executePendingTransactions();


        }
    }

    public void onPause() {

        super.onPause();
    }

    @Override
    public void onDestroy()
    {


        super.onDestroy();
    }

    private String buildRefreshText()
    {
        StringBuilder sb = new StringBuilder();

        DateFormat d = new SimpleDateFormat("h:mm a");
        String t = d.format(Calendar.getInstance().getTime());

        sb.append("Last Refreshed: ").append(t);



        return sb.toString();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
