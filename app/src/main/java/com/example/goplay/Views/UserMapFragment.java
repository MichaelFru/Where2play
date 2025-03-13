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
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.goplay.FBAuthHelper;
import com.example.goplay.FireUserHelper;
import com.example.goplay.FireVenueHelper;
import com.example.goplay.ImageUtils;
import com.example.goplay.R;
import com.example.goplay.controller.NotificationWorker;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class UserMapFragment extends Fragment implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Marker currentLocationMarker;
    private HashMap<Marker, Venue> venueMarkerMap = new HashMap<>();
    private FireVenueHelper fireVenueHelper;
    private FireUserHelper fireUserHelper;
    private TextView tvName, tvType, tvCapacity, tvPlaying;
    private ImageView ivVenueImage;
    private Button btnGoPlay, btnLeave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps26, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map26);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        fireVenueHelper = new FireVenueHelper(null);
        fireUserHelper = new FireUserHelper(null);
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

        loadVenuesFromFirestore();
        mMap.setOnMarkerClickListener(this::onMarkerClick);
    }

    private void loadVenuesFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("venues").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Venue venue = document.toObject(Venue.class);
                    venue.setDocId(document.getId());
                    LatLng location = new LatLng(venue.getLatitude(), venue.getLongtitude());

                    Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(venue.getName()));
                    venueMarkerMap.put(marker, venue);
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

        tvName = dialog.findViewById(R.id.tvVenueName_dialog);
        tvType = dialog.findViewById(R.id.tvVenueType_dialog);
        tvCapacity = dialog.findViewById(R.id.tvVenueCapacity_dialog);
        tvPlaying = dialog.findViewById(R.id.tvVenuePlaying_dialog);
        ivVenueImage = dialog.findViewById(R.id.img_dialog);
        btnGoPlay = dialog.findViewById(R.id.btn_play_dialog);
        btnLeave = dialog.findViewById(R.id.btn_leave_dialog);

        tvName.setText(venue.getName());
        tvType.setText("Type: " + venue.getType());
        tvCapacity.setText("Capacity: " + venue.getCapacity());
        tvPlaying.setText("Playing Now: " + venue.getPlaying());

        if (venue.getImage() != null && !venue.getImage().isEmpty()) {
            Bitmap bitmap = ImageUtils.convertStringToBitmap(venue.getImage());
            ivVenueImage.setImageBitmap(bitmap != null ? bitmap : null);
        } else {
            ivVenueImage.setImageResource(R.drawable.placeholder_image);
        }
     FireUserHelper.getOne(FBAuthHelper.getCurrentUser().getUid(), new FireUserHelper.FBReply() {
         @Override
         public void getAllSuccess(ArrayList<User> users) {
         }

         @Override
            public void getOneSuccess(User user) {
            if (user.getCurrentVenue()!=null && user.getCurrentVenue().getDocId()==venue.getDocId()){
                btnLeave.setVisibility(View.VISIBLE);
                btnGoPlay.setVisibility(View.GONE);
            } else if (user.getCurrentVenue() != null && user.getCurrentVenue().getDocId()!=venue.getDocId()){
                btnGoPlay.setVisibility(View.GONE);
                btnLeave.setVisibility(View.GONE);
                }
            }
        });

        btnGoPlay.setOnClickListener(v -> {
            String docId = venue.getDocId();
            FireUserHelper.getOne(currentUser.getUid(), new FireUserHelper.FBReply() {
                @Override
                public void getAllSuccess(ArrayList<User> users) {}

                @Override
                public void getOneSuccess(User user) {
                    if (venue.getPlaying() < venue.getCapacity() ) {
                        Toast.makeText(getContext(), "Going to play!", Toast.LENGTH_SHORT).show();
                        fireVenueHelper.incVenuePlayers(docId);
                        fireUserHelper.setPlayingVenue(venue ,currentUser.getUid() );

                        // updating instantly the playing in the dialog
                        FirebaseFirestore.getInstance().collection("venues").document(docId).get()
                                .addOnSuccessListener(documentSnapshot -> updatePlayingCount(documentSnapshot));

                        btnGoPlay.setVisibility(View.GONE);
                        btnLeave.setVisibility(View.VISIBLE);


                        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                                .setInitialDelay(5, TimeUnit.SECONDS)
                                .build();
                        WorkManager.getInstance(getContext()).enqueue(workRequest);
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
                public void getAllSuccess(ArrayList<User> users) {}

                @Override
                public void getOneSuccess(User user) {
                    fireVenueHelper.decVenuePlayers(docId);
                    fireUserHelper.removePlayingVenue(currentUser.getUid());

                    FirebaseFirestore.getInstance().collection("venues").document(docId).get()
                            .addOnSuccessListener(documentSnapshot -> updatePlayingCount(documentSnapshot));

                    btnGoPlay.setVisibility(View.VISIBLE);
                    btnLeave.setVisibility(View.GONE);
                }
            });
        });

        dialog.show();
    }

    private void updatePlayingCount(DocumentSnapshot documentSnapshot) {
        if (documentSnapshot.exists()) {
            int updatedPlaying = documentSnapshot.getLong("playing").intValue();
            tvPlaying.setText("Now Playing: " + updatedPlaying);
        }
    }

    @SuppressLint("MissingPermission")
    private void setupLiveLocationUpdates() {
        mMap.setMyLocationEnabled(true);
        fusedLocationClient.requestLocationUpdates(LocationRequest.create(), new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult.getLastLocation() != null) {
                    LatLng currentLocation = new LatLng(locationResult.getLastLocation().getLatitude(),
                            locationResult.getLastLocation().getLongitude());

                    if (currentLocationMarker == null) {
                        currentLocationMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("You are here"));
                    } else {
                        currentLocationMarker.setPosition(currentLocation);
                    }
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));
                }
            }
        }, null);
    }
}
