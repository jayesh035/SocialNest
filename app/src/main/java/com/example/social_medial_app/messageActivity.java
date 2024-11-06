package com.example.social_medial_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.social_medial_app.Models.User;
import com.example.social_medial_app.adapters.MessageAdapter;
import com.example.social_medial_app.databinding.ActivityMessageBinding;
import com.example.social_medial_app.utils.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class messageActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.social_medial_app.databinding.ActivityMessageBinding binding = ActivityMessageBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        ArrayList<User>userList=new ArrayList<>();
        MessageAdapter adapter =new MessageAdapter(this,userList);
        binding.messageRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.messageRv.setAdapter(adapter);
        db=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

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
                    else
                    {
                        binding.username.setText(user.Name);
                    }

                }
                userList.clear();
                userList.addAll(tmpList);
                adapter.notifyDataSetChanged();
            }
        });



    }
}