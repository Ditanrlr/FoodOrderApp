package com.android.foodorderapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity {
    private Button button,buttonr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        button = findViewById(R.id.button);
        buttonr = findViewById(R.id.buttonr);

        View decorView = getWindow().getDecorView();

        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        button.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), loginSession.class));
        });
        buttonr.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), registerSession.class));
        });
    }
}