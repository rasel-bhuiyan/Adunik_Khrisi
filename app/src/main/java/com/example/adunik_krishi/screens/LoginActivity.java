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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText emailET,passwordET;
    private Button loginBTN;
    private ProgressBar loadingBar;
    private String email,password;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();

        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = emailET.getText().toString();
                password = passwordET.getText().toString();


                if(email.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Enter your gmail", Toast.LENGTH_SHORT).show();
                }
                else if(password.isEmpty() || password.length() < 6){
                    Toast.makeText(LoginActivity.this, "Enter 6 digit password", Toast.LENGTH_SHORT).show();
                }
                else{
                    loginUser();
                }
            }
        });
    }

    private void loginUser() {

        loadingBar.setVisibility(View.VISIBLE);
        loginBTN.setVisibility(View.INVISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    reload();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                loadingBar.setVisibility(View.INVISIBLE);
                loginBTN.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initialize() {
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        loginBTN = findViewById(R.id.loginBTN);
        loadingBar = findViewById(R.id.loadingBar);

        mAuth = FirebaseAuth.getInstance();
    }

    public void create_new(View view) {
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
    }

    public void forget_password(View view) {
        startActivity(new Intent(LoginActivity.this, OTPActivity.class));
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
        startActivity(new Intent(LoginActivity.this, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}