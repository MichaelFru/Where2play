package com.example.goplay;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.goplay.databinding.ActivityMapsBinding;

public class MapsActivity26 extends Fragment implements OnMapReadyCallback  {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_maps26, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map26);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        return view;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d("MapsActivity", "onMapReady called");

        mMap = googleMap;

        if (mMap == null) {
            Log.e("MapsActivity", "GoogleMap is null");
            return; // Prevent further operations if mMap is null
        }

        // Check location permissions
        if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Enable My Location layer
            mMap.setMyLocationEnabled(true);

            // Use FusedLocationProviderClient to get the user's current location
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(view.getContext());
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            // Get the current location coordinates
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                            // Add a marker at the current location and move the camera
                            mMap.addMarker(new MarkerOptions().position(currentLocation).title("You are here"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("MapsActivity", "Failed to get current location", e);
                    });
        } else {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }
    }

//        } else {
//            // Request location permissions if not granted
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1 && grantResults.length > 0) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Reinitialize the map when permissions are granted
//                if (mMap != null) {
//                    onMapReady(mMap);
//                }
//            } else {
//                // Show a message if the permission is denied
//                Toast.makeText(this, "Permission denied. Cannot access location.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }




