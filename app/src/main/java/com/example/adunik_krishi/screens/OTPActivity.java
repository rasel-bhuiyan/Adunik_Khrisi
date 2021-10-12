package com.example.adunik_krishi.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adunik_krishi.R;
import com.example.adunik_krishi.constant.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {

    private EditText otpET;
    private Button loginBTN;
    private TextView statusTV;
    private ProgressBar loadingBar;

    private String name,phone,city,password;
    private String mVerificationId;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    PhoneAuthCredential phoneAuthCredential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        initialize();

        sendVerificationCode();

        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otpET.getText().toString();

                if(code.isEmpty()){
                    Toast.makeText(OTPActivity.this, "কোড দিয়ে পরে বাটন ক্লিক করুন", Toast.LENGTH_SHORT).show();
                }
                else if(code.length() == 6){
                    loadingBar.setVisibility(View.VISIBLE);
                    loginBTN.setVisibility(View.INVISIBLE);
                    verifyCode(code);
                }
                else{
                    Toast.makeText(OTPActivity.this, "আপানি সঠিক কোড ব্যবহার করেন নি ।", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendVerificationCode() {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+88"+phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            Log.d("TAG", "onVerificationCompleted:" + credential);

            String code = credential.getSmsCode();

            if(code != null){
                otpET.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            Log.w("TAG", "onVerificationFailed", e);

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(OTPActivity.this, "কিছু সমস্যা এর জন্য এখন OTP পাঠানো যাচ্ছে না ।", Toast.LENGTH_SHORT).show();
                // Invalid request
            } else if (e instanceof FirebaseTooManyRequestsException) {
                Toast.makeText(OTPActivity.this, "আপনি অনেক বার চেষ্টা করে ফেলেছেন ।", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {

            Log.d("TAG", "onCodeSent:" + verificationId);

            mVerificationId = verificationId;
        }
    };

    private void verifyCode(String code) {

        phoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId,code);
        signInWithPhoneAuthCredential(phoneAuthCredential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

               if(task.isSuccessful()){

                   Toast.makeText(OTPActivity.this, "OTP Verify সম্পন্ন হয়েছে ।", Toast.LENGTH_SHORT).show();

                   makeUserDatabase();

                   Intent intent = new Intent(OTPActivity.this,RegisterActivity.class);
                   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                   startActivity(intent);
               }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                loadingBar.setVisibility(View.INVISIBLE);
                loginBTN.setVisibility(View.VISIBLE);

                Toast.makeText(OTPActivity.this, "OTP Verify করা যাচ্ছে না । আবার চেষ্টা করুন ।", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makeUserDatabase() {

        String uID = mAuth.getCurrentUser().getUid();

        HashMap<String,Object> dataMap = new HashMap<>();

        dataMap.put("uID",uID);
        dataMap.put("phone",phone);
        dataMap.put("name",name);
        dataMap.put("city",city);
        dataMap.put("password",password);

        databaseReference.child(phone).setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Toast.makeText(OTPActivity.this, "আপনার একাউন্ট খুলা হয়েছে ।", Toast.LENGTH_SHORT).show();

                    saveLoginInfo();

                    Intent intent = new Intent(OTPActivity.this,RegisterActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                loadingBar.setVisibility(View.INVISIBLE);
                loginBTN.setVisibility(View.VISIBLE);

                Toast.makeText(OTPActivity.this, "আপনার একাউন্ট খুলা যায় নি ।", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveLoginInfo() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("phone",phone);
        editor.putString("password",password);
        editor.putBoolean("isLogin",true);
        editor.apply();

    }

    private void initialize() {

        otpET = findViewById(R.id.otpET);
        loginBTN = findViewById(R.id.loginBTN);
        loadingBar = findViewById(R.id.loadingBar);
        statusTV = findViewById(R.id.statusTV);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance(Constant.DATABASE_REFERENCE).getReference("users");

        phone = getIntent().getStringExtra(RegisterActivity.PHONE);
        name = getIntent().getStringExtra(RegisterActivity.NAME);
        city = getIntent().getStringExtra(RegisterActivity.CITY);
        password = getIntent().getStringExtra(RegisterActivity.PASSWORD);
    }
}