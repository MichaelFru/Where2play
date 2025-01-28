package com.example.goplay;

import android.util.Log;

import com.example.goplay.database_classes.User;
import com.example.goplay.database_classes.Venue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FireVenueHelper {
    private static final String TAG = "FireStoreHelper Tag";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private static CollectionReference collectionRef = db.collection("venues");
    private FireVenueHelper.FBReply fbReply;

    public interface FBReply {
        void getAllSuccess(ArrayList<Venue> venues);
        void getOneSuccess(Venue venue);
    }

    public FireVenueHelper(FireVenueHelper.FBReply fbReply) {
        this.fbReply = fbReply;
    }

    public void add(Venue venue) {
        collectionRef.add(venue).addOnSuccessListener(documentReference -> {
            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
        }).addOnFailureListener(e -> {
            Log.w(TAG, "Error adding document", e);
        });
    }

    public void update(String id, Venue venue) {
        collectionRef.document(id).update("name", venue.getName(), "capacity", venue.getCapacity(), "image", venue.getImage()).addOnSuccessListener(aVoid -> {
            Log.d(TAG, "DocumentSnapshot updated with ID: " + id);
        }).addOnFailureListener(e -> {
            Log.w(TAG, "Error updating document", e);
        });
    }

    public void delete(String id) {
        collectionRef.document(id).delete().addOnSuccessListener(aVoid -> {
            Log.d(TAG, "DocumentSnapshot deleted with ID: " + id);
        }).addOnFailureListener(e -> {
            Log.w(TAG, "Error deleting document", e);
        });
    }
    public void getAll() {
        collectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Venue> venues = new ArrayList<>();
                for (com.google.firebase.firestore.DocumentSnapshot document : task.getResult()) {
                    Log.d(TAG, document.getId() + " => " + document.getData());
                    Venue venue = document.toObject(Venue.class);
                    venues.add(venue);
                }
                fbReply.getAllSuccess(venues);
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });

    }

    public void getOne(String id) {
        collectionRef.document(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                com.google.firebase.firestore.DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    Venue venue = document.toObject(Venue.class);
                    fbReply.getOneSuccess(venue);
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }
    public static CollectionReference getCollectionRef() {
        return collectionRef;
    }
}


