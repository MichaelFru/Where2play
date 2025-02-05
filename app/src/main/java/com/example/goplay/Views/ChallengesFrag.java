package com.example.goplay.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.goplay.FBAuthHelper;
import com.example.goplay.FireUserHelper;
import com.example.goplay.R;
import com.example.goplay.model.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChallengesFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChallengesFrag extends Fragment implements FireUserHelper.FBReply {
    TextView textView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChallengesFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment challenges.
     */
    // TODO: Rename and change types and number of parameters
    public static ChallengesFrag newInstance(String param1, String param2) {
        ChallengesFrag fragment = new ChallengesFrag();
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
        View view =  inflater.inflate(R.layout.fragment_challenges, container, false);
        textView = view.findViewById(R.id.profile_name);
        FireUserHelper fireUserHelper = new FireUserHelper(this);
        fireUserHelper.getOne(FBAuthHelper.getCurrentUser().getUid());
        return view;
    }

    @Override
    public void getAllSuccess(ArrayList<User> users) {

    }

    @Override
    public void getOneSuccess(User user) {
        textView.setText(user.getName());
    }
}