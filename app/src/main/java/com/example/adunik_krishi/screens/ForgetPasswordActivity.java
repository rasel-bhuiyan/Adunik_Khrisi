package com.example.adunik_krishi.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adunik_krishi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText emailET;
    private TextView statusTV;
    private Button forgetBTN;
    private ProgressBar loadingBar;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        initialize();

        forgetBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailET.getText().toString();


                if(email.isEmpty()){
                    Toast.makeText(ForgetPasswordActivity.this, "Enter your gmail", Toast.LENGTH_SHORT).show();
                }
                else{
                    sendResetPassword();
                }
            }
        });
    }

    private void sendResetPassword() {

        forgetBTN.setVisibility(View.INVISIBLE);
        loadingBar.setVisibility(View.VISIBLE);

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            statusTV.setText("Password Reset Link send Successfully to your gmail. Check and reset your password.");
                            loadingBar.setVisibility(View.INVISIBLE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ForgetPasswordActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                forgetBTN.setVisibility(View.VISIBLE);
                loadingBar.setVisibility(View.INVISIBLE);
            }
        });
    }


    private void initialize() {

        emailET = findViewById(R.id.emailET);
        forgetBTN = findViewById(R.id.forgetBTN);
        loadingBar = findViewById(R.id.loadingBar);
        statusTV = findViewById(R.id.statusTV);
    }
}