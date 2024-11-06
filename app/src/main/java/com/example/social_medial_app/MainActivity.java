package com.example.social_medial_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//
               if(FirebaseAuth.getInstance().getCurrentUser()==null)
               {
                   // Intent to switch to ActivityB
                   Intent intent = new Intent(MainActivity.this, signUpActivity.class);
                   startActivity(intent);
                   finish();  // Close ActivityA
               }
               else {
                   Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                   startActivity(intent);
                   finish();
               }

            }
        }, 3000);  // 3000 milliseconds delay
    }
}