package com.example.social_medial_app.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.social_medial_app.Models.User;
import com.example.social_medial_app.R;
import com.example.social_medial_app.adapters.SearchRvAdapter;
import com.example.social_medial_app.databinding.FragmentSearchBinding;
import com.example.social_medial_app.utils.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;


public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public SearchFragment() {
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
        binding=FragmentSearchBinding.inflate(inflater,container,false);
        ArrayList<User>userList=new ArrayList<>();
        SearchRvAdapter adapter=new SearchRvAdapter(getContext(),userList);
        binding.rv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        binding.rv.setAdapter(adapter);
        db=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        db.collection(Constants.USER_MODE).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
           ArrayList<User>tmpList=new ArrayList<>();
           for(DocumentSnapshot ds:queryDocumentSnapshots.getDocuments())
           {

               User user=ds.toObject(User.class);
               assert user != null;
               if(!Objects.equals(user.Email, Objects.requireNonNull(auth.getCurrentUser()).getEmail()))
               {
                   tmpList.add(user);
               }

           }
           userList.clear();
           userList.addAll(tmpList);
           adapter.notifyDataSetChanged();
            }
        });

        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = binding.searchView.getText().toString().trim();
                if (text.isEmpty()) {
                    // Handle the empty search case
                    Toast.makeText(getContext(), "Please enter a name to search", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d("SearchFragment", "Search button clicked: " + text);
                db.collection(Constants.USER_MODE)
                        .whereEqualTo("Name", text)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.getDocuments().isEmpty()) {
                                    ArrayList<User> tmpList = new ArrayList<>();
                                    for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                                        User user = ds.toObject(User.class);
                                        assert user != null;
                                        if (!Objects.equals(user.Email, Objects.requireNonNull(auth.getCurrentUser()).getEmail())) {
                                            tmpList.add(user);
                                        }
                                    }
                                    userList.clear();
                                    userList.addAll(tmpList);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(getContext(), "No users found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e("SearchFragment", "Error fetching data", e);
                            Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        return binding.getRoot();
    }
}