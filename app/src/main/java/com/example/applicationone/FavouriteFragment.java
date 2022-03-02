package com.example.applicationone;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.applicationone.trains.ServiceArray;
import com.example.applicationone.trains.TrainService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouriteFragment extends Fragment {

    SwipeRefreshLayout refresh;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView.LayoutManager layoutManager;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment favouriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouriteFragment newInstance(String param1, String param2) {
        FavouriteFragment fragment = new FavouriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        try {
            ServiceArray.clearTrains();

            new TrainService("NWP","CDF", true);
            new TrainService("CDF","NWP", true);
            //train3 = new TrainService("SWA", "PAD", true);

            TrainServiceAdapter adapter = new TrainServiceAdapter();
            RecyclerView listView = view.findViewById(R.id.recyclerList);
            listView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
            layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            listView.setLayoutManager(layoutManager);
            listView.setAdapter(adapter);

            refresh = view.findViewById(R.id.recyclerListRefresh);
            refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    try {
                        refresh.setRefreshing(true);
                        ServiceArray.updateTrains();
                        adapter.notifyDataSetChanged();
                        refresh.setRefreshing(false);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

/*
            trains.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                //TODO (1) -> Implement custom adapter you can update instead of remaking a train.
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        ServiceArray.findByIndex(i).reload();
                        arrayAdapter = new ArrayAdapter(
                                getActivity().getApplicationContext(), R.layout.custom_list, ServiceArray.getStrings()
                        );
                        trains.setAdapter(arrayAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

 */

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return view;
    }
}