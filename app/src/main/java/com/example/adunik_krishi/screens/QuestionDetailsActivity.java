package com.example.adunik_krishi.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adunik_krishi.R;
import com.example.adunik_krishi.adapters.CommentAdapter;
import com.example.adunik_krishi.constant.Constant;
import com.example.adunik_krishi.models.Comment;
import com.example.adunik_krishi.models.Product;
import com.example.adunik_krishi.models.Question;
import com.example.adunik_krishi.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class QuestionDetailsActivity extends AppCompatActivity {

    private ImageView questionImageView;
    private TextView questionTitleTV,questionDetailsTV,questionDateTV,questionAdminTV;
    private EditText questionCommentET;
    private Button questionCommentAddBTN;
    private RecyclerView commentRecyclerView;
    private List<Comment> commentList;
    private CommentAdapter commentAdapter;

    private ProgressDialog loadingBar;

    private String qID,phone;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference,userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_details);

        initialize();

        Loading();

        getAllData();

        setCommentData();

        questionCommentAddBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = questionCommentET.getText().toString();

                if(comment.isEmpty()){
                    Toast.makeText(QuestionDetailsActivity.this, "কমেন্ট বক্স খালি থাকা যাবে না", Toast.LENGTH_SHORT).show();
                }
                else{

                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    boolean isLogin = preferences.getBoolean("isLogin",false);
                    String phone = preferences.getString("phone",null);

                    if(currentUser != null){
                        addComment(comment,phone);
                    }
                    else if(isLogin){
                        addComment(comment,phone);
                    }
                    else{
                        startActivity(new Intent(QuestionDetailsActivity.this, LoginActivity.class));
                    }
                }
            }
        });
    }

    private void setCommentData() {

        DatabaseReference commentRef = databaseReference.child("comments");
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    commentList.clear();

                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Comment comment = dataSnapshot.getValue(Comment.class);
                        commentList.add(comment);
                        commentAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addComment(String comment,String phone) {

        LoadingComment();

        DatabaseReference commentRef = databaseReference.child("comments");

        String randomValue = UUID.randomUUID().toString();

        HashMap<String,Object> commentMap = new HashMap<>();
        commentMap.put("comment",comment);
        commentMap.put("phone",phone);
        commentMap.put("cID",randomValue);

        commentRef.child(randomValue).setValue(commentMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(QuestionDetailsActivity.this, "আপনার কমেন্ট যুক্ত করা হয়েছে", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    questionCommentET.setText("");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QuestionDetailsActivity.this, "আপনার কমেন্ট যুক্ত করা হয় নি", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        });

    }

    private void getAllData() {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    Question question = snapshot.getValue(Question.class);

                    questionTitleTV.setText(question.getqTitle());
                    questionDetailsTV.setText(question.getqDetails());
                    questionDateTV.setText("তারিখ - "+question.getqDate());
                    Glide.with(getApplicationContext()).load(question.getqImage()).into(questionImageView);

                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                User user = snapshot.getValue(User.class);

                                questionAdminTV.setText("প্রশ্ন করেছেন - "+user.getName());

                                loadingBar.dismiss();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            loadingBar.dismiss();
                            Toast.makeText(getApplicationContext(), "কিছু সমস্যা এর জন্য প্রশ্ন এর তথ্য লোড হয় নি ।", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    loadingBar.dismiss();
                    Toast.makeText(getApplicationContext(), "কিছু সমস্যা এর জন্য পণ্য এর প্রশ্ন লোড হয় নি ।", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingBar.dismiss();
                Toast.makeText(getApplicationContext(), "কিছু সমস্যা এর জন্য প্রশ্ন এর তথ্য লোড হয় নি ।", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialize() {

        questionImageView = findViewById(R.id.question_imageView);
        questionTitleTV = findViewById(R.id.question_titleTV);
        questionDetailsTV = findViewById(R.id.question_detailsTV);
        questionDateTV = findViewById(R.id.question_dateTV);
        questionAdminTV = findViewById(R.id.question_adminTV);
        questionCommentET = findViewById(R.id.question_commentET);
        questionCommentAddBTN = findViewById(R.id.question_commentAddBTN);
        commentRecyclerView = findViewById(R.id.questionCommentRecyclerView);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentList,this);
        commentRecyclerView.setAdapter(commentAdapter);
        loadingBar = new ProgressDialog(this);


        qID = getIntent().getStringExtra("qID");
        phone = getIntent().getStringExtra("phoneNumber");

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance(Constant.DATABASE_REFERENCE).getReference("questions").child(qID);

        userRef = FirebaseDatabase.getInstance(Constant.DATABASE_REFERENCE).getReference("users").child(phone);
    }

    private void Loading() {

        loadingBar.setTitle("প্রশ্ন এর তথ্য আসছে");
        loadingBar.setMessage("দয়া করে অপেক্ষা করুন...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }

    private void LoadingComment() {

        loadingBar.setTitle("কমেন্ট যুক্ত করা হচ্ছে");
        loadingBar.setMessage("দয়া করে অপেক্ষা করুন...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }
}