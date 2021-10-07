package com.example.adunik_krishi;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class ChasabadPoddoti extends AppCompatActivity implements View.OnClickListener {
    CardView danchash, wheathChash;


    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chasabad_poddoti);

        danchash = findViewById(R.id.C_Danchash);
        wheathChash = findViewById(R.id.C_WheatChash);

        danchash.setOnClickListener(this);
        wheathChash.setOnClickListener(this);


        // list view
        ArrayList<String> itemlist = new ArrayList<String>();
        itemlist.add("ধান চাষ");
        itemlist.add("গম চাষ");
        itemlist.add("আলু চাষ");
        itemlist.add("বেগুন চাষ");
        itemlist.add("পাট চাষ");
        itemlist.add("ভূট্টা চাষ");
        itemlist.add("সরিষা চাষ");
        itemlist.add("সয়ানি চাষ");

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemlist);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.C_Danchash:
                Intent intentDanchash = new Intent(getApplicationContext(), DanChasa.class);
                startActivity(intentDanchash);
                break;
            case R.id.C_WheatChash:

                break;

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.search_view, menu);
        MenuItem menuItem = menu.findItem(R.id.search_view);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("আপনার ফসলটি লিখুন");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

}