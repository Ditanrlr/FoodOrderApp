package com.android.foodorderapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.auth.UserProfileChangeRequest;

public class registerSession extends Activity {
    private EditText editText, editText1, editText2, editText3;
    private Button button,buttonr;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    TextView tx1;
    int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editText = findViewById(R.id.editText);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        button = findViewById(R.id.button);
        buttonr = findViewById(R.id.buttonr);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(registerSession.this);
        progressDialog.setTitle(("Loading"));
        progressDialog.setMessage("Silahkan tunggu");
        progressDialog.setCancelable(false);

        button.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), loginSession.class));
        });
        buttonr.setOnClickListener(view -> {
            if (editText.getText().length()>0 && editText1.getText().length()>0 && editText2.getText().length()>0 && editText3.getText().length()>0){
                if (editText2.getText().toString().equals(editText3.getText().toString())){
                    register(editText.getText().toString(), editText1.getText().toString(), editText2.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(),"Password tidak sama!!",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(),"Silahkan isi semua data!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void register(String Nama,String Email,String Password){
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() && task.getResult()!=null){
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    if (firebaseUser!=null) {
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                .setDisplayName(Nama)
                                .build();
                        firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                reload();
                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(),"gagal register", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private  void reload(){
        startActivity(new Intent(getApplicationContext(),loginSession.class));
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
