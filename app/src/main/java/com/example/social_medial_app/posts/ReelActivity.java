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
import com.example.social_medial_app.Models.Reel;
import com.example.social_medial_app.Models.User;
import com.example.social_medial_app.R;
import com.example.social_medial_app.databinding.ActivityReelBinding;
import com.example.social_medial_app.utils.Constants;
import com.example.social_medial_app.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ReelActivity extends AppCompatActivity {
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.social_medial_app.databinding.ActivityReelBinding binding = ActivityReelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();  //
// Launcher to upload reel
        final String[] videoURL = {null};

        final ActivityResultLauncher<String> launcher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri o) {
                        if (o != null) {
                            // Start the upload process and handle the result asynchronously
                            Utils.uploadVideo(o, ReelActivity.this, Constants.REEL_FOLDER)
                                    .addOnCompleteListener(new OnCompleteListener<String>() {
                                        @Override
                                        public void onComplete(@NonNull Task<String> task) {
                                            if (task.isSuccessful()) {
                                                videoURL[0] = task.getResult();
                                            } else {
                                                // Handle failure case
                                                Exception e = task.getException();
                                                Toast.makeText(ReelActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    }
                }
        );

        binding.selectIReel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcher.launch("video/*");
            }
        });

        binding.cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReelActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();

            }
        });

        binding.postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String caption = Objects.requireNonNull(binding.caption.getEditText()).getText().toString();
                final User[] user = new User[1];

                FirebaseAuth auth = FirebaseAuth.getInstance();
                String uid = Objects.requireNonNull(auth.getCurrentUser()).getUid() + Constants.REEL;
                db.collection(Constants.USER_MODE).document(Objects.requireNonNull(auth.getCurrentUser()).getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        documentSnapshot.get()
                        Reel reel;
                        user[0] = documentSnapshot.toObject(User.class);
                        assert user[0] != null;
                        assert videoURL[0] != null;
                        reel = new Reel(videoURL[0], caption,user[0].Image);

                        db.collection(Constants.REEL)
                                .add(reel)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        // Successfully added the post, now adding the post under the user's uid collection
                                        db.collection(uid)
                                                .add(reel)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        // Post successfully added under both collections
                                                        Intent intent = new Intent(ReelActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(e -> {
                                                    // Handle any errors in adding to the user's collection
                                                    Toast.makeText(ReelActivity.this, "Failed to reel post to user collection: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                });
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    // Handle any errors in adding to the Constants.POST collection
                                    Toast.makeText(ReelActivity.this, "Failed to upload post: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                });
                    }
                });


            }
        });

    }
}