package com.example.adunik_krishi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.adunik_krishi.adapters.ProductAdapter;
import com.example.adunik_krishi.constant.Constant;
import com.example.adunik_krishi.models.Product;
import com.example.adunik_krishi.screens.AddProductActivity;
import com.example.adunik_krishi.screens.LoginActivity;
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


public class BazarJatKoron extends Fragment {


    public BazarJatKoron() {
        // Required empty public constructor
    }

    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private List<Product> productList;
    private ProductAdapter adapter;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private ProgressDialog loadingBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_bazar_jat_koron, container, false);

        floatingActionButton = view.findViewById(R.id.addProductFBTN);
        mAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.recylerView);
        loadingBar = new ProgressDialog(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        productList = new ArrayList<>();
        adapter = new ProductAdapter(productList,getActivity());
        recyclerView.setAdapter(adapter);

        Loading();

        databaseReference = FirebaseDatabase.getInstance(Constant.DATABASE_REFERENCE).getReference("products");

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
                    Toast.makeText(getActivity(), "কোন পণ্য পাওয়া যায় নি ।", Toast.LENGTH_SHORT).show();
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

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                boolean isLogin = preferences.getBoolean("isLogin",false);

                if(currentUser != null){
                    startActivity(new Intent(getActivity(), AddProductActivity.class));
                }
                else if(isLogin){
                    startActivity(new Intent(getActivity(), AddProductActivity.class));
                }
                else{
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }

            }
        });

        return view;
    }

    private void Loading() {

        loadingBar.setTitle("নতুন পণ্য যুক্ত হচ্ছে");
        loadingBar.setMessage("দয়া করে অপেক্ষা করুন...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }
}