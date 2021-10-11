package com.tugcearas.androidmaps;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener{

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this); //used in address processing. Links with GoogleMap.OnMapLongClickListener

        //LocationMAnager :It is used to receive updates from the geographic location of the user.
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                /*
                //System.out.println("Location: " + location.toString());
                LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,1));

                 */

                /*

                // Getting an address
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                //getfromlocation da sonuç döneceğinden o sonuç yada sonuçlar listede tutulmalı
                try {
                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    // kontrol
                    if (addressList != null && addressList.size()>0){
                        System.out.println("Address: " + addressList.get(0).toString());
                    }

                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                 */


            }
        };

        // get location permission from the user
        // ACCESS_FINE_LOCATION 'nın level'ı dangerous olduğu için
        // ContextCompat is used if we want it to be compatible with previous versions.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            // permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        else {

            //location
            // if permission has already been granted ;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

            // get and show the user's last location
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            // Checking whether the user has their last position
            if (lastLocation != null) {
                LatLng userlastLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userlastLocation).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userlastLocation,15));
            }
        }

        /*

        LatLng eiffel = new LatLng(48.8583736, 2.2922926);
        mMap.addMarker(new MarkerOptions().position(eiffel).title("Eiffel Tower"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eiffel,15));

         */
    }


    // The method we write what happens after granting permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length > 0){
            if( requestCode == 1){
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                   locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    // Processing the address
    // We typed Google.MapLongClickListener to get the address clicked by the user
    // The place where the situations that should happen when the user presses the map for a long time
    @Override
    public void onMapLongClick(LatLng latLng) {

        mMap.clear();
        String address = "" ;

        // Geocoder : It takes latitude and longitude and a list of addresses returns.
        Geocoder geocoder = new Geocoder(getApplicationContext(),Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            //control
            if (addressList != null && addressList.size()>0){
                if (addressList.get(0).getThoroughfare() != null){  //we took the street
                    address += addressList.get(0).getThoroughfare();

                    if (addressList.get(0).getSubThoroughfare() != null){
                        address += addressList.get(0).getSubThoroughfare();

                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMap.addMarker(new MarkerOptions().position(latLng).title(address));


    }
}