package com.example.garbagecollectingapp;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.CancellationSignal;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.Executor;


public class MapFragment extends Fragment {

    LocationManager locationManager;
    LocationListener locationListenerGPS;

    private static final int INITIAL_REQUEST=1337;
    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;

    MapFragment(LocationManager locationManager){
        this.locationManager = locationManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view
        View view = inflater.inflate(R.layout.fragment_map,container, false);
        //Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        // preparing scaled bitmap image for truck icon
        int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.dumptruck);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap truckIcon = Bitmap.createScaledBitmap(b, width, height, false);

        //Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                LiveLocationViewer.getInstance(googleMap).forceStop();
                locationListenerGPS = new LocationListener() {
                    @Override
                    public void onLocationChanged(android.location.Location location) {
                        // Getting latitude of the current location
                        double latitude = location.getLatitude();
                        // Getting longitude of the current location
                        double longitude = location.getLongitude();
                        float speed = location.getSpeed();
                        // Creating a LatLng object for the current location
                        LatLng latLng = new LatLng(latitude, longitude);
                        // Showing the current location in Google Map
                        CameraPosition camPos = new CameraPosition.Builder()
                                .target(new LatLng(latitude, longitude))
                                .zoom(10)
                                .bearing(location.getBearing())
                                .tilt(0)
                                .build();
                        CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);
                        googleMap.animateCamera(camUpdate);
                        LiveLocationViewer.getInstance(googleMap).setCurrentLocation(new Location(longitude,latitude));
                    }
                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {
                    }
                    @Override
                    public void onProviderEnabled(String s) {
                    }
                    @Override
                    public void onProviderDisabled(String s) {
                    }
                };
                if (locationManager != null) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
                        return;
                    }
                    // get the current location of the user
                    android.location.Location location = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        LiveLocationViewer.getInstance(googleMap).setCurrentLocation(new Location(location.getLongitude(),location.getLatitude()));
                    }
                    // track location changes of the user
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, 5000, 10, locationListenerGPS);
                }

                LiveLocationViewer.getInstance(googleMap).setTruckIcon(truckIcon);
                LiveLocationViewer.getInstance(googleMap).startViewUpdate();
            }
        });
        //return view
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager!=null&&locationListenerGPS!=null){
            // stop tracking user's location as he leaves the page
            locationManager.removeUpdates(locationListenerGPS);
        }
        // stop updating the map when user leaves the page
        LiveLocationViewer.getInstance(null).forceStop();
    }
}