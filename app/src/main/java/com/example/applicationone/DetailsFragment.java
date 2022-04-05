package com.example.applicationone;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.applicationone.trains.ServiceArray;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment implements TrainServiceAdapter.OnClickListener {

    private RecyclerView.LayoutManager layoutManager;
    SwipeRefreshLayout refresh;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance(String pos) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println(mParam1);
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        int pos = Integer.valueOf(mParam1);

        TrainServiceAdapter adapter = new TrainServiceAdapter(
                ServiceArray.getActiveTrains()
                .get(pos)
                .getLaterTrains()
                , this
        );
        adapter.notifyDataSetChanged();
        RecyclerView listView = view.findViewById(R.id.detailsList);
        listView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(adapter);

        refresh = view.findViewById(R.id.detailsListRefresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    refresh.setRefreshing(true);
                    ServiceArray.getActiveTrains().get(pos).reload();
                    adapter.notifyDataSetChanged();
                    refresh.setRefreshing(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Toolbar myToolbar = (Toolbar) getActivity().findViewById(R.id.mainToolbar);

        myToolbar.setNavigationIcon(R.drawable.ic_arrow);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myToolbar.setNavigationIcon(null);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new FavouriteFragment())
                        .commit();
            }
        });

        return view;
    }

    @Override
    public void onItemClick(int position) {
        //Do nothing
    }
}