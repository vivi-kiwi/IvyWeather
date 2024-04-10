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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FrameLayout framelayout;
    private TextView textView;
    private Button button;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){

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
            startFragment();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PermissionChecker.checkPermissions(MainActivity.this))
                {
                    startFragment();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Location Not Allowed", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void startFragment()
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

}