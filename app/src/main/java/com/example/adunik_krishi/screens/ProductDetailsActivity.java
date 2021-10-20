package com.example.adunik_krishi.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adunik_krishi.R;
import com.example.adunik_krishi.constant.Constant;
import com.example.adunik_krishi.models.Product;
import com.example.adunik_krishi.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ProductDetailsActivity extends AppCompatActivity {

    private ImageView product_imageView;
    private TextView product_nameTV,product_detailsTV,product_amountTV,product_quantityTV,product_adminTV,product_adminCityTV;
    private Button product_callBTN;
    private ProgressDialog loadingBar;

    private String productID,phone;


    private DatabaseReference databaseReference,userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        initialize();

        getPermission();

        Loading();

        getAllData();

        product_callBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+880"+phone));
                startActivity(intent);
            }
        });
    }

    private void getPermission() {

        String[] permissions = {Manifest.permission.CALL_PHONE};

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions,100);
        }
    }

    private void getAllData() {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    Product product = snapshot.getValue(Product.class);

                    product_nameTV.setText(product.getpName());
                    product_detailsTV.setText(product.getpDetails());
                    product_amountTV.setText("দাম - "+product.getpAmount());
                    product_quantityTV.setText("পরিমাণ - "+product.getpQuantity());
                    Glide.with(getApplicationContext()).load(product.getpImage()).into(product_imageView);

                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                User user = snapshot.getValue(User.class);

                                product_adminTV.setText("বিক্রি করবেন - "+user.getName());
                                product_adminCityTV.setText("মালিকের ঠিকানা - "+user.getCity());

                                loadingBar.dismiss();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            loadingBar.dismiss();
                            Toast.makeText(getApplicationContext(), "কিছু সমস্যা এর জন্য পণ্য এর তথ্য লোড হয় নি ।", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    loadingBar.dismiss();
                    Toast.makeText(getApplicationContext(), "কিছু সমস্যা এর জন্য পণ্য এর তথ্য লোড হয় নি ।", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingBar.dismiss();
                Toast.makeText(getApplicationContext(), "কিছু সমস্যা এর জন্য পণ্য এর তথ্য লোড হয় নি ।", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialize() {

        product_imageView = findViewById(R.id.product_imageView);
        product_nameTV = findViewById(R.id.product_nameTV);
        product_detailsTV = findViewById(R.id.product_detailsTV);
        product_amountTV = findViewById(R.id.product_amountTV);
        product_quantityTV = findViewById(R.id.product_quantityTV);
        product_adminTV = findViewById(R.id.product_adminTV);
        product_adminCityTV = findViewById(R.id.product_adminCityTV);
        product_callBTN = findViewById(R.id.product_callBTN);
        loadingBar = new ProgressDialog(this);

        productID = getIntent().getStringExtra("productID");
        phone = getIntent().getStringExtra("phoneNumber");

        databaseReference = FirebaseDatabase.getInstance(Constant.DATABASE_REFERENCE).getReference("products").child(productID);

        userRef = FirebaseDatabase.getInstance(Constant.DATABASE_REFERENCE).getReference("users").child(phone);

    }

    private void Loading() {

        loadingBar.setTitle("পণ্য এর তথ্য আসছে");
        loadingBar.setMessage("দয়া করে অপেক্ষা করুন...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }
}