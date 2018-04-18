package com.example.asus_rv.hackathon;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Mapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(10, 10)).title("Tienda 1"));
        map.addMarker(new MarkerOptions().position(new LatLng(20, 20)).title("Tienda 2"));
        map.addMarker(new MarkerOptions().position(new LatLng(30, 30)).title("Tienda 3"));
        map.addMarker(new MarkerOptions().position(new LatLng(40, 40)).title("Tienda 4"));
        map.addMarker(new MarkerOptions().position(new LatLng(50, 50)).title("Tienda 5"));
    }



}
