package com.example.garbagecollectingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DriverMapFragment extends Fragment {

    private static final int INITIAL_REQUEST=1337;
    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;

    LocationManager locationManager;
    String driverId;
    LocationListener locationListenerGPS;

    DriverMapFragment(LocationManager locationManager, String driverId) {
        this.locationManager = locationManager;
        this.driverId = driverId;
    }

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

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
            locationListenerGPS = new LocationListener() {
                @Override
                public void onLocationChanged(android.location.Location location) {
                    updateDriverLocation(location,googleMap);
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
                // get current location of the driver
                android.location.Location location = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    updateDriverLocation(location,googleMap);
                }
                // update driver location as he moves
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 5000, 10, locationListenerGPS);
            }
        }
    };

    /**
     * Update driver's location on the map as well as sending signals to database
     * @param location
     * @param googleMap
     */
    private void updateDriverLocation(android.location.Location location, GoogleMap googleMap){
        Location driverLocation = new Location(location.getLongitude(), location.getLatitude());
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
        LatLng currentLocation = new LatLng(latitude, longitude);
        // show current location of the driver to himself
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions().position(currentLocation).title("My Location")).showInfoWindow();
        updateDriverLocationInDatabase(driverLocation);
    }

    /**
     * Update driver's live location in database
     * @param location
     */
    private void updateDriverLocationInDatabase(Location location){
        DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference();
        Map toBeUpdated = new HashMap();
        toBeUpdated.put("location",location);
        db_ref.child("Driver").child(driverId).updateChildren(toBeUpdated);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_driver_map, container, false);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager!=null&&locationListenerGPS!=null){
            // stop tracking driver's location changes
            locationManager.removeUpdates(locationListenerGPS);
        }
        //activate only if you want to see truck drivers using application live
        updateDriverLocationInDatabase(null);
    }
}