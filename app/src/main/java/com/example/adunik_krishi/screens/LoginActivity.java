package com.example.adunik_krishi.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.adunik_krishi.MainActivity;
import com.example.adunik_krishi.R;
import com.example.adunik_krishi.constant.Constant;
import com.example.adunik_krishi.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText phoneET,passwordET;
    private Button loginBTN;
    private ProgressBar loadingBar;
    private String phone,password;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    boolean isNumberFound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();

        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phone = phoneET.getText().toString();
                password = passwordET.getText().toString();


                if(phone.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Enter your phone", Toast.LENGTH_SHORT).show();
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

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    isNumberFound = false;

                    for(DataSnapshot data: snapshot.getChildren()){

                        User user = data.getValue(User.class);

                        if(user.getPhone().equals(phone)){


                            if(user.getPassword().equals(password)){

                                isNumberFound = true;

                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("phone",phone);
                                editor.putString("password",password);
                                editor.putBoolean("isLogin",true);
                                editor.apply();

                                reload();

                            }
                            else {
                                isNumberFound = true;
                                Toast.makeText(LoginActivity.this, "আপনার দেয়া Password টি ভুল", Toast.LENGTH_SHORT).show();
                                loadingBar.setVisibility(View.INVISIBLE);
                                loginBTN.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    if(!isNumberFound){
                        loadingBar.setVisibility(View.INVISIBLE);
                        loginBTN.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "এই ফোন নম্বরে কোন Account পাওয়া যায় নি", Toast.LENGTH_SHORT).show();
                    }


                }
                else{
                    Toast.makeText(LoginActivity.this, "এই ফোন নম্বরে কোন Account পাওয়া যায় নি", Toast.LENGTH_SHORT).show();
                    loadingBar.setVisibility(View.INVISIBLE);
                    loginBTN.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "যান্ত্রিক ত্রুটি এর জন্য লগইন সম্ভব হয় নি, আবার চেষ্টা করুন ।", Toast.LENGTH_SHORT).show();
                loadingBar.setVisibility(View.INVISIBLE);
                loginBTN.setVisibility(View.VISIBLE);
            }
        });

    }

    private void initialize() {
        phoneET = findViewById(R.id.phoneET);
        passwordET = findViewById(R.id.passwordET);
        loginBTN = findViewById(R.id.loginBTN);
        loadingBar = findViewById(R.id.loadingBar);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance(Constant.DATABASE_REFERENCE).getReference("users");
    }

    public void create_new(View view) {
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
    }


    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isLogin = preferences.getBoolean("isLogin",false);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            reload();
        }
        else if(isLogin){
            reload();
        }



    }

    private void reload() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}