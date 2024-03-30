package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.databinding.FragmentWeatherItemBinding;


public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.ViewHolder> {

    private String[] values;
    public WeatherRecyclerViewAdapter(String[] values)
    {
        this.values = values;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentWeatherItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        holder.locationText.setText(values[position]);
    }

    @Override
    public int getItemCount()
    {
        return values.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView locationText;

        public ViewHolder(FragmentWeatherItemBinding binding) {
            super(binding.getRoot());
            locationText = binding.locationName;

        }


    }
}