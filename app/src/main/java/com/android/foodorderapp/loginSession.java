package com.android.foodorderapp;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class loginSession extends Activity  {
    private EditText editText, editText2;
    private Button button,buttonr;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_session);
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        button = findViewById(R.id.button);
        buttonr = findViewById(R.id.buttonr);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(loginSession.this);
        progressDialog.setTitle(("Loading"));
        progressDialog.setMessage("Silahkan tunggu");
        progressDialog.setCancelable(false);

        buttonr.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), registerSession.class));
        });
        button.setOnClickListener(view -> {
            if(editText.getText().length()>0 && editText2.getText().length()>0){
                login(editText.getText().toString(),editText2.getText().toString());
            }else{
                Toast.makeText(getApplicationContext(),"Silahkan isi semua data!!!", Toast.LENGTH_SHORT).show();
            }
        });
       }
       private void login(String Email, String Password){
        mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful() && task.getResult()!=null){
                    if(task.getResult().getUser()!=null){
                        reload();
                    }else {
                        Toast.makeText(getApplicationContext(),"gagal login",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"gagal login",Toast.LENGTH_SHORT).show();
                }
            }
        });
       }

    private  void reload(){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }
    }

