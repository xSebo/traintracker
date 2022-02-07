package com.example.applicationone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.applicationone.trains.StationMatcher;
import com.example.applicationone.trains.TrainService;

import org.apache.http.auth.AuthenticationException;

import java.net.URISyntaxException;

import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    Button exampleButton;
    TextView trainText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //R is shortcut for resources
        exampleButton = findViewById(R.id.nwpcdfButton);
        trainText = findViewById(R.id.trainText);
    }

    public void loadData(View v) throws InterruptedException {
        String[] locations = v.getTag().toString().split(",");
        TrainService train = new TrainService(locations[0],locations[1], true);
        trainText.setText(train.toString());
        //StationMatcher.searchStation("cheltenham");
    }
}
