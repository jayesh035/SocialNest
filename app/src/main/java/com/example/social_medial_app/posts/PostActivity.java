package com.example.social_medial_app.posts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.social_medial_app.HomeActivity;
import com.example.social_medial_app.Models.Post;
import com.example.social_medial_app.R;
import com.example.social_medial_app.databinding.ActivityPostBinding;
import com.example.social_medial_app.utils.Constants;
import com.example.social_medial_app.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class PostActivity extends AppCompatActivity {


    private FirebaseFirestore db;

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.social_medial_app.databinding.ActivityPostBinding binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.materialToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //code for upload post
        db = FirebaseFirestore.getInstance();  // <-
        final String[] imageUrl = {null};
        final ActivityResultLauncher<String> launcher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri o) {
                        if (o != null) {
                            // Start the upload process and handle the result asynchronously
                            Utils.uploadImage(o,PostActivity.this, Constants.POST_FOLDER).addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    if (task.isSuccessful()) {
                                        imageUrl[0] = task.getResult();
                                        binding.selectImage.setImageURI(o);

                                    } else {
                                        // Handle failure case
                                        Exception e = task.getException();
                                        // Log or show an error message
                                    }
                                }
                            });
                        }
                    }
                }
        );
        binding.selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcher.launch("image/*");
            }
        });

        binding.cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();

            }
        });

        binding.postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Post post;
                String caption = Objects.requireNonNull(binding.caption.getEditText()).getText().toString();


                FirebaseAuth auth = FirebaseAuth.getInstance();
                String uid = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                long currentTimeMillis = System.currentTimeMillis();
                String timeInMillis = Long.toString(currentTimeMillis);
                post = new Post(imageUrl[0], caption,uid,timeInMillis);
                db.collection(Constants.POST)
                        .add(post)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                // Successfully added the post, now adding the post under the user's uid collection
                                db.collection(uid)
                                        .add(post)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                // Post successfully added under both collections
                                                Intent intent = new Intent(PostActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            // Handle any errors in adding to the user's collection
                                            Toast.makeText(PostActivity.this, "Failed to add post to user collection: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        });
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Handle any errors in adding to the Constants.POST collection
                            Toast.makeText(PostActivity.this, "Failed to upload post: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });

            }
        });
//        Intent it=new Intent(PostActivity.this, HomeActivity.class);
//        startActivity(it);
//        finish();

    }
}