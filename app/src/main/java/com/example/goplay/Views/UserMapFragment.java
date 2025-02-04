package com.example.goplay.Views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.goplay.R;
import com.example.goplay.model.Venue;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class UserMapFragment extends Fragment implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Marker currentLocationMarker;
    private FirebaseFirestore db;
    private CollectionReference venuesRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps26, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map26);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        db = FirebaseFirestore.getInstance();
        venuesRef = db.collection("venues"); // Adjust collection name if needed

        return view;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            setupLiveLocationUpdates();
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        loadVenuesOnMap();
    }

    private void loadVenuesOnMap() {
        venuesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Venue venue = document.toObject(Venue.class);
                    LatLng venueLocation = new LatLng(venue.getLatitude(), venue.getLongtitude());

                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(venueLocation)
                            .title(venue.getName())
                            .snippet("Type: " + venue.getType() + "\nCapacity: " + venue.getCapacity())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    );
                    marker.setTag(venue);
                }

                mMap.setOnMarkerClickListener(marker -> {
                    Venue clickedVenue = (Venue) marker.getTag();
                    if (clickedVenue != null) {
                        Log.d("Venue Clicked", "Name: " + clickedVenue.getName());
                    }
                    return false; // Allow default marker behavior
                });

            } else {
                Log.e("Firestore", "Failed to fetch venues", task.getException());
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void setupLiveLocationUpdates() {
        mMap.setMyLocationEnabled(true);

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        locationCallback = new LocationCallback() {
          //  @Override
          //  public void onLocationResult(@NonNull LocationResult locationResult) {
          //      if (locationResult == null || locationResult.getLastLocation() == null) return;
//
          //      LatLng currentLocation = new LatLng(locationResult.getLastLocation().getLatitude(),
          //              locationResult.getLastLocation().getLongitude());
//
          //      if (currentLocationMarker == null) {
          //          currentLocationMarker = mMap.addMarker(new MarkerOptions()
          //                  .position(currentLocation)
          //                  .title("You are here"));
          //      } else {
          //          currentLocationMarker.setPosition(currentLocation);
          //      }
//
          //      mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14));
          //  }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupLiveLocationUpdates();
            } else {
                Log.e("MapsActivity", "Location permission denied.");
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }
}
