package com.example.goplay.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goplay.FBAuthHelper;
import com.example.goplay.FireUserHelper;
import com.example.goplay.MainActivity;
import com.example.goplay.R;
import com.example.goplay.model.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFrag extends Fragment  {

    private TextView textView;
    private FireUserHelper fireUserHelper;
    private EditText etName;
    private Button btnConfirm;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFrag newInstance(String param1, String param2) {
        SettingsFrag fragment = new SettingsFrag();
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
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);
        textView = view.findViewById(R.id.profile_name);
        fireUserHelper = new FireUserHelper(new FireUserHelper.FBReply() {
            @Override
            public void getAllSuccess(ArrayList<User> users) {

            }

            @Override
            public void getOneSuccess(User user) {

            }
        });
        fireUserHelper.getOne(FBAuthHelper.getCurrentUser().getUid(), new FireUserHelper.FBReply() {
            @Override
            public void getAllSuccess(ArrayList<User> users) {

            }

            @Override
            public void getOneSuccess(User user) {
                textView.setText(user.getName());
            }
        });
        etName = view.findViewById(R.id.nameEditText);
        btnConfirm = view.findViewById(R.id.updateBtn);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              fireUserHelper.update(FBAuthHelper.getCurrentUser().getUid(), new User(etName.getText().toString()));
                                              Toast.makeText(getContext(),"name is updated", Toast.LENGTH_SHORT).show();
                                          }
                                      });



        return view;
    }
}