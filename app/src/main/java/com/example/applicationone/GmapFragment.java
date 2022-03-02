package com.example.applicationone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GmapFragment extends Fragment {

    FusedLocationProviderClient client;

    public static LatLng currentLocation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED){

            getCurrentLocation();

        }else{
            System.out.println("Requesting Permissions");
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},100);
        }

    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            try {
                googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Marker at your location"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(currentLocation)
                        .zoom(17).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }catch(Exception e){
                e.printStackTrace();
                //TODO -> FIX THIS, ONLY LOADS LOCATION ON SECOND LOAD.
                LatLng temp = new LatLng(0,0);
                googleMap.addMarker(new MarkerOptions().position(temp).title("Marker at 0,0"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(temp));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(temp)
                        .zoom(17).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100 && (grantResults.length > 0)
        && grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED){
            getCurrentLocation();
        }else{
            Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    //https://www.youtube.com/watch?v=VdCQoJtNXAg
    @SuppressLint("MissingPermission")
    public void getCurrentLocation(){
        LocationManager lm = (LocationManager) getActivity()
                .getSystemService(Context.LOCATION_SERVICE);

        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            Executor executor = Executors.newFixedThreadPool(1);
            //CompletionService<LatLng> completionService =
                    new ExecutorCompletionService<>(executor);
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @RequiresApi(api = Build.VERSION_CODES.S)
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if(location != null) {
                        System.out.println("Submitting previous location to future");
                        currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
                        //completionService.submit(() -> new LatLng(location.getLatitude(),location.getLongitude()));
                    }else{
                        System.out.println("Building Location");
                        LocationRequest lr = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        LocationCallback lcb = new LocationCallback(){
                            @Override
                            public void onLocationResult(LocationResult lr){
                                Location location1 = lr.getLastLocation();
                                System.out.println("Submitting new location to future");
                                currentLocation = new LatLng(location1.getLatitude(),location1.getLongitude());
                                //completionService.submit(() -> new LatLng(location.getLatitude(),location.getLongitude()));
                            }
                        };
                        client.requestLocationUpdates(lr, lcb, Looper.myLooper());
                    }
                }
            });
            /*
            try {
                Future<LatLng> resultFuture = completionService.take();
                currentLocation = resultFuture.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

             */
        }else{
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gmap, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}