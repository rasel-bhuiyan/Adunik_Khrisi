package com.example.adunik_krishi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Jute_Chas extends AppCompatActivity {
    Button chaspoddoti,rogbalai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jute_chas);

        chaspoddoti = findViewById(R.id.PatChas_prokria);
        rogbalai = findViewById(R.id.Pat_rogbalai);

        chaspoddoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Jute_Chas.class));
            }
        });

    }
}