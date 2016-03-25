package com.fitflo.fitflo;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by cj on 2/2/16.
 */
public class MapCallback implements OnMapReadyCallback {

    private GoogleApiClient mGoogleApiClient;
    public Marker curMarker;
    private GoogleMap mGoogleMap;
    public MapCallback(GoogleApiClient api) {
        mGoogleApiClient = api;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("map", "hit the callback");
        mGoogleMap = googleMap;
        try {
            Location myLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            double lat = myLocation.getLatitude();
            double lng = myLocation.getLongitude();
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15);
            googleMap.animateCamera(update);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if(curMarker != null) {
                    curMarker.remove();
                }
                curMarker = mGoogleMap.addMarker(new MarkerOptions().position(latLng).draggable(true));

            }
        });
    }
}
