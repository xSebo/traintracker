package com.example.applicationone;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import com.example.applicationone.trains.ServiceArray;
import com.example.applicationone.trains.Station;
import com.example.applicationone.trains.StationMatcher;
import com.example.applicationone.trains.TrainService;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * create an instance of this fragment.
 */
public class EditFavourites extends Fragment {
    Button submitButton;
    AutoCompleteTextView fromStation;
    AutoCompleteTextView toStation;
    Toolbar myToolbar;

    List<String> fromArray = new ArrayList<>();
    List<String> toArray = new ArrayList<>();

    String[] fromTo = new String[2];

    public EditFavourites() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_favourites, container, false);

        myToolbar = getActivity().findViewById(R.id.mainToolbar);

        // This doesn't work? Why?
        OnBackPressedCallback callBack = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new FavouriteFragment()).commit();
                myToolbar.setNavigationIcon(null);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callBack);
        // ------------------------

        submitButton = view.findViewById(R.id.addStations);
        fromStation = view.findViewById(R.id.fromStation);
        toStation = view.findViewById(R.id.toStation);

        TrainSearchAdapter fromAdapter = new TrainSearchAdapter(getContext(), R.layout.station_info, StationMatcher.getStations());
        fromStation.setAdapter(fromAdapter);
        fromStation.setThreshold(0);

        TrainSearchAdapter toAdapter = new TrainSearchAdapter(getContext(), R.layout.station_info, StationMatcher.getStations());
        toStation.setAdapter(toAdapter);
        toStation.setThreshold(0);

        fromStation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                fromStation.setText(StationMatcher.getStations().get(i).getCode());
            }
        });
        toStation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                toStation.setText(StationMatcher.getStations().get(i).getCode());
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromTo[0] = fromStation.getText().toString().trim().toUpperCase();
                fromTo[1] = toStation.getText().toString().trim().toUpperCase();
                try {
                    if (!ServiceArray.alreadyExists(fromTo)) {
                        new TrainService(fromTo[0], fromTo[1], true);
                    }
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new FavouriteFragment()).commit();
                    myToolbar.setNavigationIcon(null);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }
}