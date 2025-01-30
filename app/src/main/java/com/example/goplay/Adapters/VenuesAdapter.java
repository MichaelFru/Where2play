package com.example.goplay.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goplay.ImageUtils;
import com.example.goplay.R;
import com.example.goplay.model.Venue;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class VenuesAdapter extends FirestoreRecyclerAdapter<Venue, VenuesAdapter.VenueViewHolder> {
    Context context;

    public VenuesAdapter(FirestoreRecyclerOptions<Venue> options, Context context){
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull VenueViewHolder holder, int position, @NonNull Venue venue) {
        holder.tvId.setText(venue.getVenue_id());
        holder.tvName.setText(venue.getName());
        holder.tvType.setText(venue.getType());
        holder.tvCapacity.setText(venue.getCapacity());
        holder.tvLatitude.setText( Long.toString(venue.getLatitude()));
        holder.tvLongtitude.setText( Long.toString(venue.getLongtitude()));
        if (venue.getImage() != null && !venue.getImage().isEmpty())
            holder.ivImage.setImageBitmap(ImageUtils.convertStringToBitmap(venue.getImage()));

       // holder.itemView.setOnClickListener(v -> {
       //     Intent intent = new Intent(context,Add)
       // });
    }

    @NonNull
    @Override
    public VenueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.venue_item, parent, false);
        return new VenueViewHolder(view);
    }

    public class VenueViewHolder extends RecyclerView.ViewHolder {
        private TextView tvId, tvName, tvType, tvCapacity, tvLatitude,tvLongtitude;
        private ImageView ivImage;


        public VenueViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvVenueId);
            tvName = itemView.findViewById(R.id.tvVenueName);
            tvType = itemView.findViewById(R.id.tvVenueType);
            tvCapacity = itemView.findViewById(R.id.tvVenueCapacity);
            tvLatitude = itemView.findViewById(R.id.tvVenueLatitude);
            tvLongtitude = itemView.findViewById(R.id.tvVenueLongtitude);
            ivImage = itemView.findViewById(R.id.imageView);
        }
    }
}

