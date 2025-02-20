package com.example.goplay.Views;

import static com.example.goplay.FireUserHelper.currentUser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.goplay.FireUserHelper;
import com.example.goplay.FireVenueHelper;
import com.example.goplay.ImageUtils;
import com.example.goplay.MainActivity;
import com.example.goplay.R;
import com.example.goplay.model.User;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class UserMapFragment extends Fragment implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Marker currentLocationMarker;
    private HashMap<Marker, Venue> venueMarkerMap = new HashMap<>();
    private FireVenueHelper fireVenueHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps26, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map26);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        fireVenueHelper = new FireVenueHelper(null);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        return view;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            setupLiveLocationUpdates();
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        loadVenuesFromFirestore(); // Fetch venues from Firestore and add markers
        mMap.setOnMarkerClickListener(this::onMarkerClick);
    }

    private void loadVenuesFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("venues").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Venue venue = document.toObject(Venue.class);
                    String docId = document.getId();
                    venue.setDocId(docId);
                    LatLng location = new LatLng(venue.getLatitude(), venue.getLongtitude());

                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(venue.getName()));

                    venueMarkerMap.put(marker, venue); // Store the venue data for later use
                }
            }
        });
    }

    private boolean onMarkerClick(Marker marker) {
        Venue venue = venueMarkerMap.get(marker);
        if (venue != null) {
            showVenueDialog(venue);
        }
        return true;
    }

    private void showVenueDialog(Venue venue) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.venue_dialog);

        // Find views
        TextView tvName = dialog.findViewById(R.id.tvVenueName_dialog);
        TextView tvType = dialog.findViewById(R.id.tvVenueType_dialog);
        TextView tvCapacity = dialog.findViewById(R.id.tvVenueCapacity_dialog);
        TextView tvPlaying = dialog.findViewById(R.id.tvVenuePlaying_dialog);
        ImageView ivVenueImage = dialog.findViewById(R.id.img_dialog);
        Button btnGoPlay = dialog.findViewById(R.id.btn_play_dialog);
        Button btnLeave = dialog.findViewById(R.id.btn_leave_dialog);

        // Set venue data
        tvName.setText(venue.getName());
        tvType.setText("Type: " + venue.getType());
        tvCapacity.setText("Capacity: " + venue.getCapacity());
        tvPlaying.setText("Playing Now: " + venue.getPlaying());
        // Load Base64 image
        if (venue.getImage() != null && !venue.getImage().isEmpty()) {
            Bitmap bitmap = ImageUtils.convertStringToBitmap(venue.getImage());
            if (bitmap != null) {
                ivVenueImage.setImageBitmap(bitmap);
            } else {
                ivVenueImage.setImageResource(R.drawable.placeholder_image); // Default image if decoding fails
            }
        } else {
            ivVenueImage.setImageResource(R.drawable.placeholder_image); // Default image if no image is provided
        }


        btnGoPlay.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Going to play!", Toast.LENGTH_SHORT).show();
            String docId = venue.getDocId();
            FireUserHelper.getOne(currentUser.getUid(), new FireUserHelper.FBReply() {
                @Override
                public void getAllSuccess(ArrayList<User> users) {

                }

                @Override
                public void getOneSuccess(User user) {
                    if (venue.getPlaying() < venue.getCapacity() &&  user.getCurrentVenue() != null ) { // Ensure venue isn't full
                        fireVenueHelper.incVenuePlayers(docId);
                        tvPlaying.setText(venue.getPlaying());
                        // Toggle button visibility
                        btnGoPlay.setVisibility(View.GONE);  // Hide "Go Play"
                        btnLeave.setVisibility(View.VISIBLE); // Show "Leave"
                    } else {
                        Toast.makeText(getContext(), "The Venue is Full", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        });

        btnLeave.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Leaving venue!", Toast.LENGTH_SHORT).show();

            String docId = venue.getDocId();
            FireUserHelper.getOne(currentUser.getUid(), new FireUserHelper.FBReply() {
                @Override
                public void getAllSuccess(ArrayList<User> users) {

                }

                @Override
                public void getOneSuccess(User user) {
                    fireVenueHelper.decVenuePlayers(docId); // Decrease player count

                    // Toggle button visibility back
                    btnGoPlay.setVisibility(View.VISIBLE);
                    btnLeave.setVisibility(View.GONE);
                }
            });

        });


        dialog.show();
    }




    @SuppressLint("MissingPermission")
    private void setupLiveLocationUpdates() {
        mMap.setMyLocationEnabled(true);
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult.getLastLocation() != null) {
                    LatLng currentLocation = new LatLng(locationResult.getLastLocation().getLatitude(),
                            locationResult.getLastLocation().getLongitude());

                    if (currentLocationMarker == null) {
                        currentLocationMarker = mMap.addMarker(new MarkerOptions()
                                .position(currentLocation)
                                .title("You are here"));
                    } else {
                        currentLocationMarker.setPosition(currentLocation);
                    }
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupLiveLocationUpdates();
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
