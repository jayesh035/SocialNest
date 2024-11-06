package com.example.social_medial_app.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.social_medial_app.Models.Post;
import com.example.social_medial_app.R;
import com.example.social_medial_app.adapters.MyPostRvAdapter;
import com.example.social_medial_app.databinding.FragmentMyPostBinding;
import com.example.social_medial_app.utils.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;


public class MyPostFragment extends Fragment {
    private FirebaseFirestore db;
private FragmentMyPostBinding binding;
    public MyPostFragment() {
        // Required empty public constructor

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding=FragmentMyPostBinding.inflate(inflater,container,false);
        ArrayList<Post> postList=new ArrayList<>();
        MyPostRvAdapter adapter=new MyPostRvAdapter(requireContext(),postList);
        binding.rv.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        binding.rv.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        db.collection(uid)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<Post>tmpList=new ArrayList<>();

                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            // Assuming Post has a constructor that takes a DocumentSnapshot
                            Post post = document.toObject(Post.class);
                            tmpList.add(post);
                        }
                        postList.clear();
                        postList.addAll(tmpList);
                        adapter.notifyDataSetChanged();
                    }
                });


        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // To avoid memory leaks, set the binding to null when the view is destroyed
        binding = null;
    }


}
