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
import com.example.adunik_krishi.adapters.ProductAdapter;
import com.example.adunik_krishi.constant.Constant;
import com.example.adunik_krishi.models.Product;
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

public class BuyandSellActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private List<Product> productList;
    private ProductAdapter adapter;
    private Toolbar toolbar;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyand_sell);

        initialize();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    productList.clear();

                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Product product = dataSnapshot.getValue(Product.class);
                        productList.add(product);
                        adapter.notifyDataSetChanged();
                    }
                    loadingBar.dismiss();
                }
                else {
                    Toast.makeText(BuyandSellActivity.this, "কোন পণ্য পাওয়া যায় নি।", Toast.LENGTH_SHORT).show();
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

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BuyandSellActivity.this);
                boolean isLogin = preferences.getBoolean("isLogin",false);

                if(currentUser != null){
                    startActivity(new Intent(BuyandSellActivity.this, AddProductActivity.class));
                }
                else if(isLogin){
                    startActivity(new Intent(BuyandSellActivity.this, AddProductActivity.class));
                }
                else{
                    startActivity(new Intent(BuyandSellActivity.this, LoginActivity.class));
                }

            }
        });
    }

    private void Loading() {

        loadingBar.setTitle("নতুন পণ্য যুক্ত হচ্ছে");
        loadingBar.setMessage("দয়া করে অপেক্ষা করুন...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }

    private void initialize() {

        floatingActionButton = findViewById(R.id.addProductFBTN);
        mAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("ক্রয় ও বিক্রয়");

        recyclerView = findViewById(R.id.recylerView);
        loadingBar = new ProgressDialog(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        adapter = new ProductAdapter(productList,this,0);
        recyclerView.setAdapter(adapter);

        Loading();

        databaseReference = FirebaseDatabase.getInstance(Constant.DATABASE_REFERENCE).getReference("products");
    }
}