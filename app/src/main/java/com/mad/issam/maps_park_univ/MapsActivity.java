package com.mad.issam.maps_park_univ;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.maps.android.SphericalUtil;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;

import com.google.android.gms.maps.model.Marker;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    /**
    public void change(View view){
        String s=btn.getText().toString();
        LatLng university = new LatLng(49.496483, 0.128347);
        if(s.equals("Afficher tous les parking")){
            afficher_park_hors_zone(university);
            btn.setText("Masquer les parkings hors zone");
        }
        else{
            afficher_park_zone(university,1000);
            btn.setText("Afficher tous les parking");
        }
    }
    */


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent tout =getIntent();

        String test = tout.getStringExtra(MainActivity.TYPE);
        LatLng university = new LatLng(49.496483, 0.128347);
        if (test.equals("toutes")){
            afficher_park_hors_zone(university);
        }else if (test.equals("une")){
            afficher_park_zone(university,1000);
        }else {
            int taille = tout.getIntExtra(MainActivity.LONGEUR, 0);
            afficher_park_zone(university, taille);

        }
        //LatLng university = add_univ_circle(1000);

        //afficher_park_zone(university,1000);

        //methode_zoom(university);
    }

    private void methode_zoom(LatLng university,int b) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(university)
                .zoom(b).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @NonNull
    private LatLng add_univ_circle(int a ) {
        LatLng university = new LatLng(49.496483, 0.128347);
        mMap.addMarker(new MarkerOptions().position(university).title("Université du havre")).setIcon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(university));


        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(university);
        circleOptions.radius(a);
        circleOptions.strokeColor(Color.BLACK);
        circleOptions.fillColor(0x30ff0000);
        circleOptions.strokeWidth(2);
        mMap.addCircle(circleOptions);
        return university;
    }

    private void afficher_park_zone(LatLng university, int a) {
        mMap.clear();
        university = add_univ_circle(a);
        methode_zoom(university,14);
        try {
            JSONObject object = new JSONObject(loadJSONFromAsset());
            JSONArray data = object.getJSONArray("features");

            for(int i=0;i<data.length();i++){
                JSONObject obj=data.getJSONObject(i);
                JSONObject geometryObject=obj.getJSONObject("geometry");
                JSONObject propreObject=obj.getJSONObject("properties");
                JSONArray locationJSON_Array=geometryObject.getJSONArray("coordinates");
                String Rue_parkingJSON_Array=propreObject.getString("VOIE");
                LatLng parking = new LatLng(locationJSON_Array.getDouble(1), locationJSON_Array.getDouble(0));
                if (SphericalUtil.computeDistanceBetween(university, parking) <= a) {
                    Marker locationMarker = mMap.addMarker(new MarkerOptions().position(parking).title(Rue_parkingJSON_Array));
                    locationMarker.setIcon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void afficher_park_hors_zone(LatLng university) {
        methode_zoom(university,12);
        try {
            JSONObject object = new JSONObject(loadJSONFromAsset());
            JSONArray data = object.getJSONArray("features");

            for(int i=0;i<data.length();i++){
                JSONObject obj=data.getJSONObject(i);
                JSONObject geometryObject=obj.getJSONObject("geometry");
                JSONObject propreObject=obj.getJSONObject("properties");
                JSONArray locationJSON_Array=geometryObject.getJSONArray("coordinates");
                String Rue_parkingJSON_Array=propreObject.getString("VOIE");
                LatLng parking = new LatLng(locationJSON_Array.getDouble(1), locationJSON_Array.getDouble(0));
                if (SphericalUtil.computeDistanceBetween(university, parking) > 1) {
                    Marker locationMarker = mMap.addMarker(new MarkerOptions().position(parking).title(Rue_parkingJSON_Array));
                    locationMarker.setIcon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getApplicationContext().getAssets().open("Parking.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
