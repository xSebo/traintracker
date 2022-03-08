package com.example.applicationone;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationone.trains.ServiceArray;

public class TrainServiceAdapter extends RecyclerView.Adapter<TrainServiceAdapter.ViewHolder> {

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        public TextView textView;
        public ViewHolder(TextView v){
            super(v);
            textView = v;
        }
    }

    @NonNull
    @Override
    public TrainServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder vh = new ViewHolder(new TextView(parent.getContext()));
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TrainServiceAdapter.ViewHolder holder, int position) {
        holder.textView.setText(ServiceArray.getActiveTrains().get(position).toListView());
    }

    @Override
    public int getItemCount() {
        return ServiceArray.getActiveTrains().size();
    }
}
