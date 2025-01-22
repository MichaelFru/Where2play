package com.example.goplay;

import android.util.Log;

import com.example.goplay.database_classes.User;
import com.example.goplay.database_classes.Venue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FireVenueHelper {
    private static final String TAG = "FireStoreHelper Tag";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private static CollectionReference collectionRef = db.collection("venues");
    private FireUserHelper.FBReply fbReply;

    public FireVenueHelper(FireUserHelper.FBReply fbReply) {
        this.fbReply = fbReply;
    }

    public void add(Venue venue) {
        collectionRef.add(venue).addOnSuccessListener(documentReference -> {
            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
        }).addOnFailureListener(e -> {
            Log.w(TAG, "Error adding document", e);
        });
    }
}


