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
import com.example.applicationone.trains.TrainService;

import java.util.ArrayList;
import java.util.List;

public class TrainServiceAdapter extends RecyclerView.Adapter<TrainServiceAdapter.ViewHolder> {
    List<TrainService> trains = new ArrayList<>();
    private OnClickListener onClickListener;

    public TrainServiceAdapter(List<TrainService> inputTrains, OnClickListener onClickListener){
        trains.addAll(inputTrains);
        this.onClickListener = onClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textView;
        OnClickListener onClickListener;
        public ViewHolder(TextView v, OnClickListener onClickListener){
            super(v);
            textView = v;
            this.onClickListener = onClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnClickListener{
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public TrainServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder vh = new ViewHolder(new TextView(parent.getContext()), onClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TrainServiceAdapter.ViewHolder holder, int position) {
        holder.textView.setText(trains.get(position).toListView());
    }

    @Override
    public int getItemCount() {
        return trains.size();
    }
}
