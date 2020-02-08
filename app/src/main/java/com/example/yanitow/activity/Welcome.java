package com.example.yanitow.activity;


import com.example.yanitow.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;


public class Welcome extends AppCompatActivity {

    FloatingActionButton bt_menu;
    private GoogleMap mMap;
    private BottomSheetBehavior bottomSheetBehavior;
    LocationManager locationManager;
    private AppBarConfiguration mAppBarConfiguration;
    LocationListener locationListener;
//    ImageButton menu =  findViewById(R.id.bt_menu);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        initMapFragment();
        initComponent();
//        ImageButton menu_button = findViewById(R.id.bt_menu);
//        menu_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//////                put an action here
////                Toast.makeText(Welcome.this, "PUT YOUR OWN ACTION HERE", Toast.LENGTH_SHORT).show();
//
//            }
//        });
        bt_menu = (FloatingActionButton) findViewById(R.id.bt_menu);

        bt_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Welcome.this, "menu button clicked", Toast.LENGTH_SHORT).show();

            }
        });


    }

//    public void navigation_drawer(){
//
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
////        NavigationView navView = findViewById(R.id.nav_view);
////        NavigationUI.setupWithNavController(navView, navController);
//        DrawerLayout drawerLayout;
//
//        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//
//        AppBarConfiguration appBarConfiguration =
//                new AppBarConfiguration.Builder(navController.getGraph())
//                        .setDrawerLayout(drawerLayout)
//                        .build();
//
//
//
//
//    }


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
                    Toast.makeText(Welcome.this, "hello", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    LatLng taifa = new LatLng(5.6683371, -0.2559283);
    private void initMapFragment() {
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

    private CameraUpdate zoomingLocation() {
        return CameraUpdateFactory.newLatLngZoom(taifa, 16);
    }

}