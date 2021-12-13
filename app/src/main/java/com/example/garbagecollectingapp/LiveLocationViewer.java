package com.example.garbagecollectingapp;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LiveLocationViewer{

    private static LiveLocationViewer liveLocationViewer;
    private GoogleMap googleMap;
    private Thread viewUpdater;
    private Location currentLocation;
    private Bitmap truckIcon;

    private LiveLocationViewer(GoogleMap googleMap){
        this.googleMap = googleMap;
    }

    public static LiveLocationViewer getInstance(GoogleMap googleMap){
        if (liveLocationViewer==null){
            liveLocationViewer = new LiveLocationViewer(googleMap);
        }
        return liveLocationViewer;
    }

    /**
     * updating the map, show trucks, show your location
     */
    public void startViewUpdate(){
        if(viewUpdater!=null){
            try {
                viewUpdater.interrupt();
            }catch (Exception ex){

            }finally {
                viewUpdater = null;
            }
        }
        viewUpdater = new Thread(){
            @Override
            public void run() {
                super.run();
                while (true){
                    try {
                        // retrieving all the drivers and their location
                        DatabaseReference driverDatabase = FirebaseDatabase.getInstance().getReference().child("Driver");
                        driverDatabase.orderByChild("driverid").addListenerForSingleValueEvent(new ValueEventListener(){
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                googleMap.clear();
                                if(currentLocation!=null) {
                                    LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                                    googleMap.addMarker(new MarkerOptions().position(latLng).title("My Location")).showInfoWindow();
                                }
                                for(DataSnapshot ds : snapshot.getChildren()) {
                                    String name = ds.child("drivername").getValue(String.class);
                                    String trucknumber = ds.child("trucknumber").getValue(String.class);
                                    Double longitude = ds.child("location").child("longitude").getValue(Double.class);
                                    Double latitude = ds.child("location").child("latitude").getValue(Double.class);
                                    if(longitude!=null&&latitude!=null){
                                        // if truck driver's location is same as your location you won't see both locations.(a trick is used here)
                                        if(Double.valueOf(longitude)==currentLocation.getLongitude()&&Double.valueOf(latitude)==currentLocation.getLatitude()){
                                            longitude+=0.00001;
                                            latitude+=0.00001;
                                        }
                                        // preparing the marker to show on the map
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(new LatLng(latitude,longitude));
                                        markerOptions.title(name +" : "+trucknumber);
                                        if(truckIcon!=null){
                                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(truckIcon));
                                        }
                                        googleMap.addMarker(markerOptions);
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                googleMap.clear();
                                if(currentLocation!=null) {
                                    LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                                    googleMap.addMarker(new MarkerOptions().position(latLng).title("My Location")).showInfoWindow();
                                }
                            }
                        });
                        Thread.sleep(2500);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        };
        viewUpdater.start();
    }

    /**
     * let it stop the map updating thread(the thread is heavy resource consuming, need to minimise the usage)
     */
    public void forceStop(){
        if(viewUpdater!=null){
            try {
                viewUpdater.interrupt();
            }catch (Exception ex){

            }finally {
                viewUpdater = null;
            }
        }
        liveLocationViewer = null;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void setTruckIcon(Bitmap truckIcon) {
        this.truckIcon = truckIcon;
    }
}
