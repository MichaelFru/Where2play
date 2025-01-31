package com.example.goplay.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.goplay.Adapters.VenuesAdapter;
import com.example.goplay.FireVenueHelper;
import com.example.goplay.MainActivity;
import com.example.goplay.R;
import com.example.goplay.model.Venue;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditVenuesFrag#newInstance} factory method to
 * create an instance of this fragment.
 */

public class EditVenuesFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rvVenues;
    private VenuesAdapter adapter;
    public EditVenuesFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditVenuesFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static EditVenuesFrag newInstance(String param1, String param2) {
        EditVenuesFrag fragment = new EditVenuesFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_venues, container, false);
        rvVenues = view.findViewById(R.id.rvVenues);
        setupRecyclerView();
    return view;
    }
    private void setupRecyclerView() {
        Query query = FireVenueHelper.getCollectionRef();

//        query.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful() && task.getResult() != null) {
//                if (task.getResult().isEmpty()) {
//                    Log.e("Firestore", "No venues found!");
//                } else {
//                    for (DocumentSnapshot doc : task.getResult()) {
//                        Log.d("Firestore", "Venue Found: " + doc.getData());
//                    }
//                }
//            } else {
//                Log.e("Firestore", "Error fetching venues", task.getException());
//            }
//        });

        FirestoreRecyclerOptions<Venue> options = new FirestoreRecyclerOptions.Builder<Venue>()
                .setQuery(query, Venue.class)
                .build();

        rvVenues.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new VenuesAdapter(options, getContext());
        rvVenues.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}