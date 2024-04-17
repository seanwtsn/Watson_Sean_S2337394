package com.example.weatherapp;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.data.LocationRSS;
import com.example.weatherapp.interfaces.OnLocationSelectedListener;
import com.example.weatherapp.ui.viewmodels.WeatherViewModel;

import java.util.ArrayList;


public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.ViewHolder> {

    private String[] values;
    private String[] keys;
    private final OnLocationSelectedListener onLocationSelectedListener;
    private final WeatherViewModel model;

    private final View.OnClickListener holderOnClickerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {

        }
    };



    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public WeatherRecyclerViewAdapter(OnLocationSelectedListener onLocationSelectedListener, ArrayList<LocationRSS> values, WeatherViewModel model)
    {
        this.onLocationSelectedListener = onLocationSelectedListener;
        this.values = new String[values.size()];
        this.keys = new String[values.size()];
        for (int i = 0; i < values.size(); i++)
        {
            this.values[i] = values.get(i).getName();
            this.keys[i] = values.get(i).getRssKey();
        }

        this.model = model;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_weather_item, parent, false);
        view.setOnClickListener(holderOnClickerListener);
        //return new ViewHolder(FragmentWeatherItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        holder.locationText.setText(values[position]);
        holder.rss = keys[position];
        holder.onLocationSelectedListener = onLocationSelectedListener;

    }

    @Override
    public int getItemCount()
    {
        return this.values.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView locationText;
        private String rss;
        private OnLocationSelectedListener onLocationSelectedListener;

        @Override
        public void onClick(View v )
        {
            onLocationSelectedListener.onLocationSelected(rss);
        }

        public ViewHolder(View view)
        {
            super(view);
            locationText = view.findViewById(R.id.button);
            locationText.setOnClickListener(this);

        }



    }
}