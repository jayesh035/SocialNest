package com.example.social_medial_app.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.social_medial_app.R;
import com.example.social_medial_app.databinding.FragmentAddBinding;
import com.example.social_medial_app.posts.PostActivity;
import com.example.social_medial_app.posts.ReelActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class AddFragment extends BottomSheetDialogFragment {

    FragmentAddBinding binding;

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =FragmentAddBinding.inflate(inflater, container, false);

        binding.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(getActivity(),PostActivity.class);
                startActivity(it);
                requireActivity().finish();
            }
        });
        binding.reel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it=new Intent(getActivity(), ReelActivity.class);
                startActivity(it);
            }
        });
        return binding.getRoot();
    }
}