package com.example.social_medial_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.social_medial_app.Models.User;
import com.example.social_medial_app.databinding.ActivitySignUpBinding;
import com.example.social_medial_app.utils.Constants;
import com.example.social_medial_app.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class signUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private FirebaseFirestore db;

    // Lazy initialization for binding
    private ActivitySignUpBinding getBinding() {
        if (binding == null) {
            binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        }
        return binding;
    }


    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = getBinding();
        setContentView(binding.getRoot());
    // Initilize user object
        user = new User();
        db = FirebaseFirestore.getInstance();
        //code for upload profile image
        final ActivityResultLauncher<String> launcher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri o) {
                        if (o != null) {
                            // Start the upload process and handle the result asynchronously
                            Utils.uploadImage(o,signUpActivity.this, Constants.USER_PROFILE_FOLDER).addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    if (task.isSuccessful()) {
                                        // Set the image URL to user.Image after successful upload
                                        user.Image = task.getResult();
                                        // Update the profile image in the UI

                                        binding.profileImage.setImageURI(o);
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
//edit profile code

        if(getIntent().hasExtra("MODE") && getIntent().getIntExtra("MODE",0)==1)
        {
            binding.signUpBtn.setText("Update Profile");

            FirebaseFirestore.getInstance().collection(Constants.USER_MODE)
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        user = documentSnapshot.toObject(User.class);
                        assert user != null;
                        if (!user.Image.isEmpty()) {
                            Picasso.get().load(user.Image).into(binding.profileImage);
                        }
                        Objects.requireNonNull(binding.name.getEditText()).setText(user.Name);
                        Objects.requireNonNull(binding.email.getEditText()).setText(user.Email);
                        Objects.requireNonNull(binding.password.getEditText()).setText(user.Password);

                    });
        }
// Set up the click listener for selecting an image
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the image picker
                launcher.launch("image/*");
            }
        });
        //code for sign up
        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {


                if(getIntent().hasExtra("MODE"))
                {
                    if(getIntent().getIntExtra("MODE",0)==1)
                    {
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        String uid = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                        db.collection(Constants.USER_MODE)
                                .document(uid)
                                .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //Toast.makeText(signUpActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(signUpActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                    }
                }
                else {


                    String name = Objects.requireNonNull(binding.name.getEditText()).getText().toString(); // Use getText() to retrieve the input
                    String email = Objects.requireNonNull(binding.email.getEditText()).getText().toString();
                    String password = Objects.requireNonNull(binding.password.getEditText()).getText().toString();

                    if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(signUpActivity.this, "Please Enter All Information", Toast.LENGTH_SHORT).show();
                    } else {
                        // Proceed with sign-up logic
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseAuth auth = FirebaseAuth.getInstance();
                                            String uid = Objects.requireNonNull(auth.getCurrentUser()).getUid();

//                                        user = new User(name, email, password);
                                            user.Name = name;
                                            user.Password = password;
                                            user.Email = email;
                                            db.collection(Constants.USER_MODE)
                                                    .document(uid)
                                                    .set(user)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            //Toast.makeText(signUpActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(signUpActivity.this, HomeActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(signUpActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });


//                                        Toast.makeText(getApplicationContext(), "Register successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(signUpActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                    }
                }

            }
        });

        //code to start login activity
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startLogin = new Intent(signUpActivity.this, loginActivity.class);
                startActivity(startLogin);
                finish();
            }
        });
    }
}