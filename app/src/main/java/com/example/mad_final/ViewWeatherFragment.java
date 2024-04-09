/*
 * FILE :            ViewWeatherFragment.java
 * PROJECT :         PROG3150 - Project
 * PROGRAMMER :      Vivian Morton, Isaac Ribeiro Leao
 * FIRST VERSION :   2024 - 04 - 02
 * DESCRIPTION :     Contains the logic for the view weather fragment. Such as using the APIHandler
 */

package com.example.mad_final;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Text;


public class ViewWeatherFragment extends Fragment {

    private TextView userLatitude;

    private TextView userLongitude;
    private Handler handler = new Handler();
    private FusedLocationProviderClient fusedLocationClient;


    public ViewWeatherFragment() {
        // Required empty public constructor
    }

    public static ViewWeatherFragment newInstance(String param1, String param2) {
        return new ViewWeatherFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_view_weather, container, false);

        // Initialize your TextViews here so they are ready for updates
        userLatitude = rootView.findViewById(R.id.lat_loc);
        userLongitude = rootView.findViewById(R.id.lon_loc);


        // Inflate the layout for this fragment
        if (ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION }, 0);
            ActivityCompat.requestPermissions(requireActivity(), new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, 1);

        }


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        fusedLocationClient.getLastLocation()


                .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(Location location) {
                        String longitude = Double.toString(location.getLongitude());
                        String latitude = Double.toString(location.getLatitude());
                        userLatitude.setText("Lat: " + longitude);
                        userLongitude.setText("Lon: " + latitude);
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Toast.makeText(requireContext(), "Latitude: " + String.valueOf(location.getLatitude()), Toast.LENGTH_SHORT).show();
                            Toast.makeText(requireContext(), "Longitude: " +String.valueOf(location.getLongitude()), Toast.LENGTH_SHORT).show();
                            APIHandler apiHandler = new APIHandler(handler, getContext(), location);

                        }

                    }

                })
                .addOnFailureListener(requireActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("ViewWeatherFragment", "Failed to get location");
                    }
                });

        return rootView;
    }
}