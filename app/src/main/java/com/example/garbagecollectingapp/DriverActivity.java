package com.example.garbagecollectingapp;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class DriverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        FragmentManager frag_man = getSupportFragmentManager();
        FragmentTransaction frag_tra = frag_man.beginTransaction();
        frag_tra.replace(R.id.driver_page_frame_layout,new DriverMapFragment(locationManager, getIntent().getStringExtra("driver_id")));
        frag_tra.commit();
    }


}