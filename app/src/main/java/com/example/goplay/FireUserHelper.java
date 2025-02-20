package com.example.goplay;

import android.util.Log;
import android.widget.Toast;

import com.example.goplay.model.User;
import com.example.goplay.model.Venue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class FireUserHelper {
    private static final String TAG = "FireStoreHelper Tag";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private static CollectionReference collectionRef = db.collection("users");
    private FireUserHelper.FBReply fbReply;

    public interface FBReply {
        void getAllSuccess(ArrayList<User> users);
        void getOneSuccess(User user);
    }

    public FireUserHelper(FireUserHelper.FBReply fbReply) {
        this.fbReply = fbReply;
    }

    public void add(User user) {
        collectionRef.document(FBAuthHelper.getCurrentUser().getUid()).set(user).addOnSuccessListener(documentReference -> {
            Log.d(TAG, "DocumentSnapshot added with ID: " + FBAuthHelper.getCurrentUser().getUid());
        }).addOnFailureListener(e -> {
            Log.w(TAG, "Error adding document", e);
            });
    }

    public void update(String id, User user) {
        collectionRef.document(id).update("name", user.getName()).addOnSuccessListener(aVoid -> {
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
                ArrayList<User> users = new ArrayList<>();
                for (com.google.firebase.firestore.DocumentSnapshot document : task.getResult()) {
                    Log.d(TAG, document.getId() + " => " + document.getData());
                    User user = document.toObject(User.class);
                    users.add(user);
                }
                fbReply.getAllSuccess(users);
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });

    }
    public static void getOne(String id, FBReply fbReply) {
        collectionRef.document(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                com.google.firebase.firestore.DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    User user = document.toObject(User.class);
                    fbReply.getOneSuccess(user);
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    public void setPlayingVenue(Venue venue,String userId){
        collectionRef.document(userId).update("currentVenue", venue);
    }
    public void removePlayingVenue(String userId){
        collectionRef.document(userId).update("currentVenue", null);
    }

    public static CollectionReference getCollectionRef() {
        return collectionRef;
    }
}
