package com.example.adunik_krishi.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.adunik_krishi.MainActivity;
import com.example.adunik_krishi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameET,emailET,passwordET;
    private Button registerBTN;
    private ProgressBar loadingBar;
    private String name,email,password;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialize();

        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameET.getText().toString();
                email = emailET.getText().toString();
                password = passwordET.getText().toString();

                if(name.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Enter your name", Toast.LENGTH_SHORT).show();
                }
                else if(email.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Enter your gmail", Toast.LENGTH_SHORT).show();
                }
                else if(password.isEmpty() || password.length() < 6){
                    Toast.makeText(RegisterActivity.this, "Enter 6 digit password", Toast.LENGTH_SHORT).show();
                }
                else{
                    registerUser();
                }
            }
        });
    }

    private void registerUser() {

        loadingBar.setVisibility(View.VISIBLE);
        registerBTN.setVisibility(View.INVISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    reload();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                loadingBar.setVisibility(View.INVISIBLE);
                registerBTN.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initialize() {
        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        registerBTN = findViewById(R.id.resgisterBTN);
        loadingBar = findViewById(R.id.loadingBar);

        mAuth = FirebaseAuth.getInstance();
    }

    public void sign_in(View view) {
        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void reload() {
        startActivity(new Intent(RegisterActivity.this, MainActivity.class)
        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}