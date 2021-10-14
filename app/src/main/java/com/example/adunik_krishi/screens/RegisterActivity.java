package com.example.adunik_krishi.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.adunik_krishi.MainActivity;
import com.example.adunik_krishi.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameET, phoneET,passwordET;
    private Spinner cityET;
    private Button registerBTN;
    private ProgressBar loadingBar;
    private String name,phone,city,password;

    private FirebaseAuth mAuth;

    public static final String NAME = "names";
    public static final String PHONE = "phones";
    public static final String CITY = "cities";
    public static final String PASSWORD = "passwords";

    String[] cities = {"কুমিল্লা", "ফেনী", "ব্রাহ্মণবাড়িয়া", "রাঙ্গামাটি", "নোয়াখালী", "চাঁদপুর", "লক্ষ্মীপুর", "চট্টগ্রাম", "কক্সবাজার", "খাগড়াছড়ি", "বান্দরবান",
            "সিরাজগঞ্জ", "পাবনা", "বগুড়া", "রাজশাহী", "নাটোর", "জয়পুরহাট", "চাঁপাইনবাবগঞ্জ", "নওগাঁ","যশোর", "সাতক্ষীরা", "মেহেরপুর", "নড়াইল",
            "চুয়াডাঙ্গা", "কুষ্টিয়া", "মাগুরা", "খুলনা", "বাগেরহাট", "ঝিনাইদহ", "ঝালকাঠি", "পটুয়াখালী", "পিরোজপুর", "বরিশাল", "ভোলা", "বরগুনা",
            "সিলেট", "মৌলভীবাজার", "হবিগঞ্জ", "সুনামগঞ্জ", "নরসিংদী", "গাজীপুর", "শরীয়তপুর", "নারায়ণগঞ্জ", "টাঙ্গাইল", "কিশোরগঞ্জ",
            "মানিকগঞ্জ", "ঢাকা", "মুন্সিগঞ্জ", "রাজবাড়ী", "মাদারীপুর", "গোপালগঞ্জ", "ফরিদপুর",	"পঞ্চগড়", "দিনাজপুর", "লালমনিরহাট",
            "নীলফামারী", "গাইবান্ধা", "ঠাকুরগাঁও", "রংপুর", "কুড়িগ্রাম", "শেরপুর", "ময়মনসিংহ", "জামালপুর", "নেত্রকোণা"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialize();

        cityET.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city = cities[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = nameET.getText().toString();
                phone = phoneET.getText().toString();
                password = passwordET.getText().toString();

                if(name.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Enter your name", Toast.LENGTH_SHORT).show();
                }
                else if(phone.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Enter your phone number", Toast.LENGTH_SHORT).show();
                }
                else if(city.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Enter your city name", Toast.LENGTH_SHORT).show();
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

        Intent intent = new Intent(RegisterActivity.this,OTPActivity.class);
        intent.putExtra(NAME,name);
        intent.putExtra(PHONE,phone);
        intent.putExtra(CITY,city);
        intent.putExtra(PASSWORD,password);
        startActivity(intent);

    }

    private void initialize() {
        nameET = findViewById(R.id.nameET);
        phoneET = findViewById(R.id.phoneET);
        cityET = findViewById(R.id.cityET);
        passwordET = findViewById(R.id.passwordET);
        registerBTN = findViewById(R.id.resgisterBTN);
        loadingBar = findViewById(R.id.loadingBar);

        mAuth = FirebaseAuth.getInstance();

        setSpinner();
    }

    private void setSpinner() {

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.dropdownspinner,cities);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        cityET.setAdapter(arrayAdapter);
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