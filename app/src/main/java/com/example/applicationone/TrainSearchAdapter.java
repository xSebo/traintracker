package com.example.applicationone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.applicationone.trains.Station;

import java.util.ArrayList;
import java.util.List;

public class TrainSearchAdapter extends ArrayAdapter<Station> {
    private List<Station> stationList;
    private List<Station> originalList;
    private StationFilter filter;

    public TrainSearchAdapter(@NonNull Context context, int resource, @NonNull List<Station> stations) {
        super(context, resource, stations);

        this.stationList = stations;
        this.originalList = stations;
    }

    @Override
    public Filter getFilter(){
        if(filter == null){
            filter = new StationFilter();
        }
        return filter;
    }

    private class ViewHolder{
        TextView name;
        TextView code;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        ViewHolder holder = null;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.station_info, null);

            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.stationName);
            holder.code = convertView.findViewById(R.id.stationCode);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Station station;
        try {
            station = stationList.get(position);
        }catch(IndexOutOfBoundsException e){
            station = stationList.get(0);
        }catch(NullPointerException e){
            System.out.println("Null pointer");
            return convertView;
        }
        holder.name.setText(station.getName());
        holder.code.setText(station.getCode());

        return convertView;
    }

    private class StationFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults result = new FilterResults();
            if (charSequence != null && charSequence.length() > 0) {
                charSequence = charSequence.toString().toLowerCase();
                List<Station> filteredStations = new ArrayList<>();

                for(Station s:originalList){
                    String firstChar = String.valueOf(s.toString().toLowerCase().charAt(0));
                    if(firstChar
                            .equalsIgnoreCase(String.valueOf(charSequence.charAt(0)))){
                        if(s.toString().toLowerCase().contains(charSequence)) {
                            filteredStations.add(s);
                        }
                    }
                }
                result.count = filteredStations.size();
                result.values = filteredStations;
            }else{
                synchronized (this){
                    result.count = originalList.size();
                    result.values = originalList;
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            stationList = (ArrayList<Station>) filterResults.values;
            notifyDataSetChanged();
            clear();
            try {
                for(Station s:stationList){
                    add(s);
                }
            }catch (NullPointerException e){
                //
            }
            notifyDataSetInvalidated();
        }
    }
}
