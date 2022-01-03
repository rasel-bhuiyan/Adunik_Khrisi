package com.example.adunik_krishi.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adunik_krishi.R;
import com.example.adunik_krishi.adapters.ProductAdapter;
import com.example.adunik_krishi.adapters.QuestionAdapter;
import com.example.adunik_krishi.constant.Constant;
import com.example.adunik_krishi.models.Product;
import com.example.adunik_krishi.models.Question;
import com.example.adunik_krishi.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private TextView nameTV,phoneNoTV,cityTV;

    private RecyclerView questionRecyclerView,buySellRecyclerView;
    private QuestionAdapter questionAdapter;
    private ProductAdapter buySellAdapter;
    private List<Question> questionList;
    private List<Product> buySellList;

    private String phone;

    private DatabaseReference profileDataRef,questionDatabaseReference,buySellDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initialize();

        getProfileData();

        loadQuestionData();

        loadBuySellData();
    }

    private void loadBuySellData() {

        buySellDatabaseReference.orderByChild("pPhone").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    buySellList.clear();

                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Product product = dataSnapshot.getValue(Product.class);
                        buySellList.add(product);

                        buySellAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadQuestionData() {


        questionDatabaseReference.orderByChild("qPhone").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    questionList.clear();

                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Question question = dataSnapshot.getValue(Question.class);
                        questionList.add(question);

                        questionAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getProfileData() {

        profileDataRef.addValueEventListener(new ValueEventListener() {
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

        questionRecyclerView = findViewById(R.id.questionAndAnswersRecyclerView);
        questionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionList = new ArrayList<>();
        questionAdapter = new QuestionAdapter(questionList,this,1);
        questionRecyclerView.setAdapter(questionAdapter);

        buySellRecyclerView = findViewById(R.id.buySellRecyclerView);
        buySellRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        buySellList = new ArrayList<>();
        buySellAdapter = new ProductAdapter(buySellList,this,1);
        buySellRecyclerView.setAdapter(buySellAdapter);

        phone = getIntent().getStringExtra("phone");

        profileDataRef = FirebaseDatabase.getInstance(Constant.DATABASE_REFERENCE).getReference("users").child(phone);
        questionDatabaseReference = FirebaseDatabase.getInstance(Constant.DATABASE_REFERENCE).getReference("questions");
        buySellDatabaseReference = FirebaseDatabase.getInstance(Constant.DATABASE_REFERENCE).getReference("products");



    }
}