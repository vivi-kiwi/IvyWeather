/*
 * FILE :            MainActivity.java
 * PROJECT :         PROG3150 - Project
 * PROGRAMMER :      Vivian Morton, Isaac Ribeiro Leao
 * FIRST VERSION :   2024 - 04 - 02
 * DESCRIPTION :     Contains the logic for the main activity
 */

package com.example.mad_final;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FrameLayout framelayout;
    private TextView textView;
    private Button button;
    private Handler handler = new Handler();
    private Location userLocation;
    private JSONObject weatherData;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();
        if (id == R.id.current_weather && PermissionChecker.checkPermissions(this)) {
            startCurrentWeatherFragment();
            return true;
        } else if (id == R.id.hourly_weather && PermissionChecker.checkPermissions(this)) {
            startHourlyWeatherFragment();
            return true;
        } else if (id == R.id.weekly_weather && PermissionChecker.checkPermissions(this)) {
            startWeeklyWeatherFragment();
            return true;
        }
        else
        {
            //otherwise can't change screen
            Toast.makeText(MainActivity.this, "Location Not Allowed", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        framelayout = findViewById(R.id.fragment_frame);
        textView = findViewById(R.id.location_text);
        button = findViewById(R.id.reload_button);

        //Check permissions and prompt is necessary
        PermissionChecker.promptPrecise(this);
        PermissionChecker.promptCoarse(this);

        if(PermissionChecker.checkPermissions(this))
        {
            getLocationCallAPI();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PermissionChecker.checkPermissions(MainActivity.this))
                {
                    startCurrentWeatherFragment();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Location Not Allowed", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void startCurrentWeatherFragment()
    {
        textView.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        framelayout.setVisibility(View.VISIBLE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        ViewWeatherFragment viewWeatherFragment = new ViewWeatherFragment();
        Log.d(TAG, "App starting. Setting fragment to viewWeatherFragment");
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_frame, viewWeatherFragment)
                .commit();
    }

    private void startHourlyWeatherFragment()
    {
        textView.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        framelayout.setVisibility(View.VISIBLE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        ViewWeatherFragment viewWeatherFragment = new ViewWeatherFragment();
        Log.d(TAG, "App starting. Setting fragment to viewWeatherFragment");
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_frame, viewWeatherFragment)
                .commit();
    }

    private void startWeeklyWeatherFragment()
    {
        textView.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        framelayout.setVisibility(View.VISIBLE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        ViewWeatherFragment viewWeatherFragment = new ViewWeatherFragment();
        Log.d(TAG, "App starting. Setting fragment to viewWeatherFragment");
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_frame, viewWeatherFragment)
                .commit();
    }


    private void getLocationCallAPI()
    {
        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(Location location) {
                            String longitude = Double.toString(location.getLongitude());
                            String latitude = Double.toString(location.getLatitude());
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                userLocation = location;
                                Log.d(TAG, "Location acquired");
                                APIHandler apiHandler = new APIHandler(handler, getBaseContext(), userLocation, new APIHandler.WeatherDataListener() {
                                    @Override
                                    public void onDataFetched(JSONObject jsonData) {
                                        //Once Json data is fetched, update UI
                                        weatherData = jsonData;

                                        String locationName = getCurrentLocationName(userLocation);



                                        //send parameters to fragment
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                //put code to update ui with data here!
                                                startCurrentWeatherFragment();
                                            }
                                        });
                                    }
                                });

                            }

                        }

                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ViewWeatherFragment", "Failed to get location");
                        }
                    });
        }
        catch (SecurityException e)
        {
            Log.d(TAG, "Security Exception: " + e.getMessage());
        }

    }


    private String getCurrentLocationName(Location location) {

        //Using geocoder and storing addresses in a list
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {

            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                //Got the location, return it
                return address.getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}