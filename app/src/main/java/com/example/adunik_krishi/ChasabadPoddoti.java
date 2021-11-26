package com.example.adunik_krishi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ChasabadPoddoti extends AppCompatActivity implements View.OnClickListener {
    CardView danchash, wheathChash, potatoChas, eggChas, juteChhas, cornChas, musterdCahs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chasabad_poddoti);

        danchash = findViewById(R.id.C_Danchash);
        wheathChash = findViewById(R.id.C_WheatChash);
        potatoChas = findViewById(R.id.C_PotatoChash);
        eggChas = findViewById(R.id.C_Eggplant);
        juteChhas = findViewById(R.id.C_JuteChasahbad);
        musterdCahs = findViewById(R.id.C_MustardChash);

        danchash.setOnClickListener(this);
        wheathChash.setOnClickListener(this);
        potatoChas.setOnClickListener(this);
        eggChas.setOnClickListener(this);
        juteChhas.setOnClickListener(this);
        musterdCahs.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.C_Danchash:
                Intent intentDanchash = new Intent(getApplicationContext(), Dan_Chas.class);
                startActivity(intentDanchash);
                break;
            case R.id.C_WheatChash:
                Intent intentGomchash = new Intent(getApplicationContext(), gomChas.class);
                startActivity(intentGomchash);
                break;
            case R.id.C_PotatoChash:
                Toast.makeText(this, "under working", Toast.LENGTH_SHORT).show();
                break;
            case R.id.C_Eggplant:
                Toast.makeText(this, "under working", Toast.LENGTH_SHORT).show();
                break;
            case R.id.C_JuteChasahbad:
                    Intent intent_pat = new Intent(getApplicationContext(),Jute_Chas.class);
                    startActivity(intent_pat);
                break;
            case R.id.C_MustardChash:
                Toast.makeText(this, "under working", Toast.LENGTH_SHORT).show();
                break;

        }

    }


}