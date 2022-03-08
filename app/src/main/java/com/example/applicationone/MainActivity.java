package com.example.applicationone;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.applicationone.trains.StationMatcher;
import com.example.applicationone.trains.TrainService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements LifecycleOwner {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            StationMatcher.initialise(); //Runs once to init list
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_main); //R is shortcut for resources

        Toolbar myToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(myToolbar);

        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Fragment selectedFragment = null;
                switch(item.getItemId()) {
                    case R.id.editFavourite:
                        selectedFragment = new EditFavourites();
                        myToolbar.setNavigationIcon(R.drawable.ic_arrow);
                        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                myToolbar.setNavigationIcon(null);
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new FavouriteFragment()).commit();
                            }
                        });
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();
                return true;
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavBar);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_favourite:
                        selectedFragment = new FavouriteFragment();
                        break;
                    case R.id.nav_map:
                        selectedFragment = new GmapFragment();
                        break;
                    default:
                        selectedFragment = new FavouriteFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();
                return true;
            }
        });


        /*
        TrainService train1;
        TrainService train2;
        try {
            train1 = new TrainService("NWP","CDF", true);
            train2 = new TrainService("CDF","NWP", true);

            ServiceArray.addTrains(new TrainService[]{train1, train2});

            trains = findViewById(R.id.favouriteTrainsList);

            arrayAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    ServiceArray.getStrings()
            ); // simple_list_item_1 is a premade list, use ctrl+enter for more

            trains.setAdapter(arrayAdapter);

            trains.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                //TODO (1) -> Implement custom adapter you can update instead of remaking a train.
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String[] textArray = trains.getItemAtPosition(i).toString().split(" ");
                    String uid = textArray[textArray.length-1];
                    try {
                        ServiceArray.findByUid(uid).get().reload();
                        System.out.println(ServiceArray.getStrings());
                    } catch (Exception e) {
                        System.out.println("test");
                        e.printStackTrace();
                    }
                }
            });

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

         */


        //Intent i = new Intent(this, MainActivity2.class);
        //i.putExtra("instruction","hooray");
        //startActivity(i);

    }

}
