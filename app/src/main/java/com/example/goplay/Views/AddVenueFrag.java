package com.example.goplay.Views;

import static com.example.goplay.ImageUtils.uriToImageView;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.goplay.FireVenueHelper;
import com.example.goplay.ImageUtils;
import com.example.goplay.MainActivity;
import com.example.goplay.R;
import com.example.goplay.model.Venue;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddVenueFrag#newInstance} factory method to
 * create an instance of this fragment.
 */

//https://github.com/belindaatschool/noteslist
public class AddVenueFrag extends Fragment {
    private Button btnSubmit;
    private Button btnUploadImg;
    private EditText etVenueName;
    private EditText etVenueType;
    private EditText etVenueLatitude;
    private EditText etVenueLongtitude;
    private EditText etVenueCapacity;
    private ImageView ivImage;
    private FireVenueHelper fireVenueHelper;
    private ActivityResultLauncher<String> galleryLauncher; // Class-level variable for gallery launcher


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddVenueFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddVenueFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static AddVenueFrag newInstance(String param1, String param2) {
        AddVenueFrag fragment = new AddVenueFrag();
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
        View view = inflater.inflate(R.layout.fragment_add_venue, container, false);
        btnSubmit = view.findViewById(R.id.btn_submit);
        btnUploadImg = view.findViewById(R.id.btn_upload_image);
        etVenueName= view.findViewById(R.id.et_name);
        etVenueType= view.findViewById(R.id.et_type);
        etVenueLatitude= view.findViewById(R.id.et_latitude);
        etVenueLongtitude= view.findViewById(R.id.et_longitude);
        etVenueCapacity= view.findViewById(R.id.et_capacity);
        ivImage= view.findViewById(R.id.iv_image);
        fireVenueHelper = new FireVenueHelper(null);

        // Set drawable for ivImage - Placeholder image
        Drawable drawable1 = ContextCompat.getDrawable(getActivity(), R.drawable.app_logo);
        Bitmap bitmap1 = ((BitmapDrawable) drawable1).getBitmap();
        ivImage.setImageBitmap(bitmap1);

        // Register gallery launcher
        registerGalleryLauncher();

        btnUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch gallery to select an image
                galleryLauncher.launch("image/*");
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidInput()) {
                    Toast.makeText(getContext(), "Click", Toast.LENGTH_SHORT).show();
                    String VenueName = etVenueName.getText().toString();
                    int VenueCapacity = Integer.parseInt(etVenueCapacity.getText().toString());
                    double VenueLatitude = Double.valueOf(etVenueLatitude.getText().toString());
                    double VenueLongtitude = Double.valueOf(etVenueLongtitude.getText().toString());
                    String VenueType = etVenueType.getText().toString();
                    Bitmap bitmap = ImageUtils.getBitmapFromImageView(ivImage);
                    saveVenue(VenueName,VenueType,VenueLatitude,VenueLongtitude,VenueCapacity, bitmap);
                }
            }
        });
        return view;
    }

    private void saveVenue(String name,String type, double latitude,double longtitude, int capacity, Bitmap bitmap) {

            fireVenueHelper.add(new Venue( name, type, latitude, longtitude, capacity, ImageUtils.convertBitmapToString(bitmap)), new FireVenueHelper.AddVenueCallBack() {
                @Override
                public void OnAddSuccess() {
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();            
                }

                @Override
                public void OnAddFailure() {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            });
    }
    private boolean isValidInput() {
        boolean isValid = true;
        if (etVenueName.getText().toString().isEmpty()) {
            etVenueName.setError("Please enter some content");
            isValid = false;
        }
        if (etVenueType.getText().toString().isEmpty()) {
            etVenueType.setError("Please enter some content");
            isValid = false;
        }
        if (etVenueLatitude.getText().toString().isEmpty()) {
            etVenueLatitude.setError("Please enter some content");
            isValid = false;
        }
        if (etVenueLongtitude.getText().toString().isEmpty()) {
            etVenueLongtitude.setError("Please enter some content");
            isValid = false;
        }
        if (etVenueCapacity.getText().toString().isEmpty()) {
            etVenueCapacity.setError("Please enter some content");
            isValid = false;
        }
        return isValid;
    }

    //get image from camera
    // Register the gallery launcher
    private void registerGalleryLauncher() {
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            uriToImageView(getContext(), uri,ivImage);
                            ivImage.setImageURI(uri); // Set the selected image to the ImageView
                        }
                    }
                });
    }
}