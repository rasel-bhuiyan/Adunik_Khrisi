package com.example.adunik_krishi.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.example.adunik_krishi.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class QuestionAnswerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_answer);

        initialize();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser currentUser = mAuth.getCurrentUser();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                boolean isLogin = preferences.getBoolean("isLogin",false);

                if(currentUser != null){
                    startActivity(new Intent(QuestionAnswerActivity.this, AddQuestionActivity.class));
                }
                else if(isLogin){
                    startActivity(new Intent(QuestionAnswerActivity.this, AddQuestionActivity.class));
                }
                else{
                    startActivity(new Intent(QuestionAnswerActivity.this, LoginActivity.class));
                }
            }
        });
    }

    private void initialize() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("প্রশ্ন ব্যাংক ও উত্তর");
        recyclerView = findViewById(R.id.questionRecyclerView);
        floatingActionButton = findViewById(R.id.floatingActionBTN);

        mAuth = FirebaseAuth.getInstance();
    }
}