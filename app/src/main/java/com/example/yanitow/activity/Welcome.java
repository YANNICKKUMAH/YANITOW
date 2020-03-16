package com.example.yanitow.activity;


import com.example.yanitow.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class Welcome extends AppCompatActivity {

    FloatingActionButton bt_menu;
    private GoogleMap mMap;
    private BottomSheetBehavior bottomSheetBehavior;
    LocationManager locationManager;
    private String provider;
    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    Location location;
    FusedLocationProviderClient fusedLocationProvider;

    LocationListener locationListener;
    int PERMISSION_ID =44;

//    ImageButton menu =  findViewById(R.id.bt_menu);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Toolbar toolbar = findViewById(R.id.toolbar);


        getLastLocation();
        initComponent();

        toolbar.setNavigationIcon(R.drawable.ic_menu);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
//                    drawer.closeDrawer(Gravity.RIGHT);
//                } else {
//                    drawer.openDrawer(Gravity.RIGHT);
//                }
//            }
//        });


       fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();


    }


    private void initComponent() {
        // get the bottom sheet view
        LinearLayout llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);

        // init the bottom sheet behavior
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);

        // change the state of the bottom sheet
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        ((FloatingActionButton) findViewById(R.id.fab_directions)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                try {
                    mMap.animateCamera(zoomingLocation());
                } catch (Exception e) {
                    Toast.makeText(Welcome.this, "PLEASE TRY AGAIN", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


//    LatLng taifa = new LatLng(5.6683371, -0.2559283);



//    private void initMapFragment() {
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//
//
//        mapFragment.getMapAsync(new OnMapReadyCallback() {
//
//
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//                mMap = Tools.configActivityMaps(googleMap);
//
//                MarkerOptions markerOptions = new MarkerOptions().position(taifa).title("Taifa Burkina Post Office").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                mMap.addMarker(markerOptions);
//                mMap.moveCamera(zoomingLocation());
//                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                    @Override
//                    public boolean onMarkerClick(Marker marker) {
//                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                        try {
//                            mMap.animateCamera(zoomingLocation());
//                        } catch (Exception e) {
//                        }
//                        return true;
//                    }
//                });
//            }
//
//
//        });
//    }
    LatLng taifa = new LatLng(location.getLatitude(), location.getLongitude());

    private CameraUpdate zoomingLocation() {
        return CameraUpdateFactory.newLatLngZoom(taifa, 16);
    }



    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationProvider.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


                                    mapFragment.getMapAsync(new OnMapReadyCallback() {


                                        @Override
                                        public void onMapReady(GoogleMap googleMap) {
                                            mMap = Tools.configActivityMaps(googleMap);
                                            MarkerOptions markerOptions = new MarkerOptions().position(taifa).title("Taifa Burkina Post Office").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                            mMap.addMarker(markerOptions);
                                            mMap.moveCamera(zoomingLocation());
                                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                                @Override
                                                public boolean onMarkerClick(Marker marker) {
                                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                                    try {
                                                        mMap.animateCamera(zoomingLocation());
                                                    } catch (Exception e) {
                                                    }
                                                    return true;
                                                }
                                            });
                                        }


                                    });


                                }


                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProvider.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
              // put new locationt reques into this part


//            latTextView.setText(mLastLocation.getLatitude()+"");
//            lonTextView.setText(mLastLocation.getLongitude()+"");
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }





}

