package com.example.social_medial_app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.social_medial_app.Models.Reel;
import com.example.social_medial_app.R;
import com.example.social_medial_app.adapters.ReelAdapter;
import com.example.social_medial_app.databinding.FragmentReelBinding;
import com.example.social_medial_app.utils.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;


public class ReelFragment extends Fragment {
 ReelAdapter adapter;
 private FirebaseFirestore db;
 ArrayList<Reel>reelList=new ArrayList<>();
    public ReelFragment() {
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
        com.example.social_medial_app.databinding.FragmentReelBinding binding = FragmentReelBinding.inflate(inflater,container,false);

            adapter=new ReelAdapter(requireContext(),reelList);
            binding.viewpager.setAdapter(adapter);
        db=FirebaseFirestore.getInstance();
        db.collection(Constants.REEL).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            ArrayList<Reel>tmpList=new ArrayList<>();
            reelList.clear();
            for (DocumentSnapshot ds:queryDocumentSnapshots.getDocuments())
            {
                Reel reel=ds.toObject(Reel.class);
                assert reel != null;
                if(reel.videoUrl != null && !reel.videoUrl.isEmpty())
                {
                    tmpList.add(reel);
                }

            }
            reelList.addAll(tmpList);
            reverseArrayList(reelList);
            adapter.notifyDataSetChanged();

            }
        });

        return binding.getRoot();
    }

    public static <Reel> void reverseArrayList(ArrayList<Reel> list) {
        Collections.reverse(list);
    }
}