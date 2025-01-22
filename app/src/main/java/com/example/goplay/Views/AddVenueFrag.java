package com.example.goplay.Views;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.goplay.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddVenueFrag#newInstance} factory method to
 * create an instance of this fragment.
 */

//https://github.com/belindaatschool/noteslist
public class AddVenueFrag extends Fragment {
    private Button btnSubmit;
    private Button btnUploadImg;
    private EditText etVenueId;
    private EditText etVenueName;
    private EditText etVenueType;
    private EditText etVenueLatitude;
    private EditText etVenueLongtitude;
    private EditText etVenueCapacity;
    private Bitmap bitmapVenueImg;

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
        btnSubmit = getView().findViewById(R.id.btn_submit);
        btnUploadImg = getView().findViewById(R.id.btn_upload_image);
        btnUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
}