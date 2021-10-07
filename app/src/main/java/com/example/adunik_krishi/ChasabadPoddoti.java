package com.example.adunik_krishi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ChasabadPoddoti extends AppCompatActivity implements View.OnClickListener {
    CardView danchash, wheathChash;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chasabad_poddoti);

        danchash = findViewById(R.id.C_Danchash);
       wheathChash = findViewById(R.id.C_WheatChash);

        danchash.setOnClickListener(this);
        wheathChash.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {
       switch (v.getId()) {
            case R.id.C_Danchash:
                Intent intentDanchash = new Intent(getApplicationContext(), DanChas.class);
                startActivity(intentDanchash);
                break;
            case R.id.C_WheatChash:

                break;

        }

    }




}