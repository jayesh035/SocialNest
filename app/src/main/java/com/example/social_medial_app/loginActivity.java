package com.example.social_medial_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.social_medial_app.Models.User;
import com.example.social_medial_app.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class loginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        // start signup activity
        binding.createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startSignUp=new Intent(loginActivity.this,signUpActivity.class);
                binding=ActivityLoginBinding.inflate(getLayoutInflater());
                startActivity(startSignUp);
                finish();
            }
        });
        // login funcanality

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= Objects.requireNonNull(binding.loginEmail.getEditText()).getText().toString();
                String password= Objects.requireNonNull(binding.loginPassword.getEditText()).getText().toString();

                if(email.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(loginActivity.this,"Please fill all details",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    User user=new User(email,password);
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful())
                       {
                        Intent Home=new Intent(loginActivity.this,HomeActivity.class);
                        startActivity(Home);
                        finish();
                       }else {
                           // If sign in fails, display a message to the user.
                           Toast.makeText(loginActivity.this, "Authentication Failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                       }
                        }
                    });

                }

            }
        });
    }
}