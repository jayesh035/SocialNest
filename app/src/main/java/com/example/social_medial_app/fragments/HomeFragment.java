package com.example.social_medial_app.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.social_medial_app.HomeActivity;
import com.example.social_medial_app.Models.HomePost;
import com.example.social_medial_app.Models.Post;
import com.example.social_medial_app.Models.Reel;
import com.example.social_medial_app.Models.User;
import com.example.social_medial_app.R;
import com.example.social_medial_app.adapters.HomePostRvAdapter;
import com.example.social_medial_app.adapters.ReelAdapter;
import com.example.social_medial_app.adapters.StoryAdapter;
import com.example.social_medial_app.databinding.FragmentHomeBinding;
import com.example.social_medial_app.messageActivity;
import com.example.social_medial_app.utils.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

private FirebaseFirestore db;

private FragmentHomeBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Initialize view binding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        setHasOptionsMenu(true);
        activity.setSupportActionBar(binding.materialToolbar2);


        ArrayList<User>userList=new ArrayList<>();
        StoryAdapter adapter=new StoryAdapter(getContext(),userList);
        binding.storyRv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
        binding.storyRv.setAdapter(adapter);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        db=FirebaseFirestore.getInstance();

        db.collection(auth.getCurrentUser().getUid()+Constants.FOLLOW).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
           ArrayList<User>tmpList=new ArrayList<>();

           for(DocumentSnapshot ds:queryDocumentSnapshots.getDocuments())
           {
           User us=ds.toObject(User.class);
               assert us != null;

               tmpList.add(us);


           }
                userList.clear();
           userList.addAll(tmpList);
           adapter.notifyDataSetChanged();

            }
        });

        ArrayList<HomePost> postList = new ArrayList<>();
        HomePostRvAdapter HomePostAdapter = new HomePostRvAdapter(getContext(), postList);
        binding.postRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.postRv.setAdapter(HomePostAdapter);

        db.collection(Constants.POST).get().addOnSuccessListener(queryDocumentSnapshots -> {
            ArrayList<HomePost> tmpList = new ArrayList<>();
            int totalPosts = queryDocumentSnapshots.size();
            int[] fetchedCount = {0}; // to track how many posts have been fully fetched

            for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {

                Post post_data = ds.toObject(Post.class);

                assert post_data != null;
                db.collection(Constants.USER_MODE).document(post_data.uid).get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User us = documentSnapshot.toObject(User.class);
                        if (us != null) {
                            HomePost homePost = new HomePost(post_data.postUrl, us.Image, post_data.caption, us.Name,post_data.timeStamp);
                            tmpList.add(homePost);
                        }
                    }

                    fetchedCount[0]++;
                    // Update the adapter after all user data has been fetched
                    if (fetchedCount[0] == totalPosts) {
                        postList.clear();
                        postList.addAll(tmpList);
                        HomePostAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(e -> {
                    // Handle Firestore document fetch failure
                    Log.e("HomeFragment", "Failed to fetch user document for uid: " + post_data.uid, e);
                    fetchedCount[0]++;
                    if (fetchedCount[0] == totalPosts) {
                        postList.clear();
                        postList.addAll(tmpList);
                        HomePostAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

binding.materialToolbar2.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if(item.getItemId()==R.id.message_icon)
        {
            Intent message=new Intent(getContext(), messageActivity.class);
            startActivity(message);
            return true;
        }
        return false;
    }
});

        return binding.getRoot(); // Return the root view of the binding

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // To avoid memory leaks, set the binding to null when the view is destroyed
        binding = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.option_manu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}