package com.example.adunik_krishi.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

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

public class ProfileActivity extends AppCompatActivity {

    private TextView nameTV,phoneNoTV,cityTV;

    private DatabaseReference profileDataRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initialize();

        getProfileData();
    }

    private void getProfileData() {

        profileDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    User user = snapshot.getValue(User.class);

                    nameTV.setText(user.getName());
                    phoneNoTV.setText("মোবাইল নম্বর - "+user.getPhone());
                    cityTV.setText("জেলা - "+user.getCity());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialize() {
        nameTV = findViewById(R.id.profileNameTV);
        phoneNoTV = findViewById(R.id.profilePhoneNoTV);
        cityTV = findViewById(R.id.profileCityTV);

        String phone = getIntent().getStringExtra("phone");

        profileDataRef = FirebaseDatabase.getInstance(Constant.DATABASE_REFERENCE).getReference("users").child(phone);



    }
}