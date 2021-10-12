package com.example.adunik_krishi.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.example.adunik_krishi.R;
import com.example.adunik_krishi.adapters.QuestionAdapter;
import com.example.adunik_krishi.constant.Constant;
import com.example.adunik_krishi.models.Product;
import com.example.adunik_krishi.models.Question;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuestionAnswerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private QuestionAdapter questionAdapter;
    private List<Question> questionList;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_answer);

        initialize();

        Loading();

        databaseReference = FirebaseDatabase.getInstance(Constant.DATABASE_REFERENCE).getReference("questions");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    questionList.clear();

                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Question question = dataSnapshot.getValue(Question.class);
                        questionList.add(question);
                        questionAdapter.notifyDataSetChanged();
                    }
                    loadingBar.dismiss();
                }
                else {
                    loadingBar.dismiss();
                    Toast.makeText(getApplicationContext(), "কোন প্রশ্ন পাওয়া যায় নি ।", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                loadingBar.dismiss();
            }
        });

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

    private void Loading() {

        loadingBar.setTitle("নতুন প্রশ্ন যুক্ত হচ্ছে");
        loadingBar.setMessage("দয়া করে অপেক্ষা করুন...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }

    private void initialize() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("প্রশ্ন ব্যাংক ও উত্তর");
        recyclerView = findViewById(R.id.questionRecyclerView);
        floatingActionButton = findViewById(R.id.floatingActionBTN);
        loadingBar = new ProgressDialog(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionList = new ArrayList<>();
        questionAdapter = new QuestionAdapter(questionList,getApplicationContext());
        recyclerView.setAdapter(questionAdapter);

        mAuth = FirebaseAuth.getInstance();
    }
}