package com.example.goplay.Views;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
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

        FirestoreRecyclerOptions<Venue> options = new FirestoreRecyclerOptions.Builder<Venue>()
                .setQuery(query, Venue.class)
                .build();

        adapter = new VenuesAdapter(options, requireContext());
        rvVenues.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvVenues.setAdapter(adapter);
        adapter.startListening();

        // Swipe-to-delete with background effect
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // No moving, just swipe
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                confirmDeletion(position);
            }

            @Override
            public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    Paint paint = new Paint();
                    paint.setColor(Color.RED); // Background color for delete action

                    float cornerRadius = 40f; // Adjust this for more or less rounding
                    float left, right, top, bottom;

                    if (dX > 0) { // Swiping right
                        left = itemView.getLeft();
                        right = dX;
                    } else { // Swiping left
                        left = itemView.getRight() + dX;
                        right = itemView.getRight();
                    }
                    top = itemView.getTop();
                    bottom = itemView.getBottom();

                    // Draw rounded rectangle as background
                    canvas.drawRoundRect(left, top, right, bottom, cornerRadius, cornerRadius, paint);

                    // Draw "DELETE" text
                    paint.setColor(Color.WHITE);
                    paint.setTextSize(50);
                    paint.setTextAlign(Paint.Align.CENTER);
                    float textX = (left + right) / 2;
                    float textY = itemView.getTop() + itemView.getHeight() / 2 + 15;
                    canvas.drawText("DELETE", textX, textY, paint);

                    super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        });

        itemTouchHelper.attachToRecyclerView(rvVenues);
    }

    private void confirmDeletion(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Delete Venue")
                .setMessage("Are you sure you want to delete this venue?")
                .setPositiveButton("Yes", (dialog, which) -> deleteVenue(position))
                .setNegativeButton("Cancel", (dialog, which) -> {
                    adapter.notifyItemChanged(position); // Reset swipe action
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    private void deleteVenue(int position) {
        FireVenueHelper fireVenueHelper = new FireVenueHelper(null);
        String venueId = adapter.getSnapshots().getSnapshot(position).getId();

        fireVenueHelper.delete(venueId, new FireVenueHelper.OnDeleteListener() {
            @Override
            public void onDeleteSuccess() {
                Log.d("Firestore", "Venue deleted successfully");
                adapter.notifyItemRemoved(position); // לעדכן UI אחרי הצלחה
            }

            @Override
            public void onDeleteFailure(Exception e) {
                Log.e("Firestore", "Error deleting venue", e);
                adapter.notifyItemChanged(position); // לשחזר UI במקרה של כישלון
            }
        });
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