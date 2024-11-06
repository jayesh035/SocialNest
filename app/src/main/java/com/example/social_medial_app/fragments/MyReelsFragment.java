package com.example.social_medial_app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.social_medial_app.Models.Post;
import com.example.social_medial_app.Models.Reel;
import com.example.social_medial_app.R;
import com.example.social_medial_app.adapters.MyPostRvAdapter;
import com.example.social_medial_app.adapters.MyReelRvAdapter;
import com.example.social_medial_app.databinding.FragmentMyReelsBinding;
import com.example.social_medial_app.utils.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;


public class MyReelsFragment extends Fragment {
    private FirebaseFirestore db;
private FragmentMyReelsBinding binding;
    public MyReelsFragment() {
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
        binding=FragmentMyReelsBinding.inflate(inflater, container, false);
        ArrayList<Reel> reelList=new ArrayList<>();
        MyReelRvAdapter adapter=new MyReelRvAdapter(requireContext(),reelList);
        binding.rv.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        binding.rv.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = Objects.requireNonNull(auth.getCurrentUser()).getUid()+ Constants.REEL;
        db.collection(uid)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<Reel>tmpList=new ArrayList<>();

                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            // Assuming Post has a constructor that takes a DocumentSnapshot
                            Reel reel = document.toObject(Reel.class);
                            tmpList.add(reel);
                        }
                        reelList.addAll(tmpList);
                        adapter.notifyDataSetChanged();
                    }
                });
        return binding.getRoot();
    }
}