package com.example.applicationone;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrainAdapter extends RecyclerView.Adapter<TrainAdapter.ViewHolder> {
    private static final String TAG = "TrainAdapter";

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        //public final TextView textView;

        public ViewHolder(View v){
            super(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("Test!, " + getAdapterPosition() + " clicked");
                }
            });
            //textView = (TextView) v.findViewById(R.id.textView);


        }
    }
}
