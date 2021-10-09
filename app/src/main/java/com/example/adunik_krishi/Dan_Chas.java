package com.example.adunik_krishi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class Dan_Chas extends AppCompatActivity {
    Button btn1,btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dan__chas);

        btn1 = findViewById(R.id.DanChas_prokria);
        btn2 = findViewById(R.id.Dan_rogbalai);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dan_Chas.this, "Danchasabad", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Dan_Chas.this,Dan_Chas.class);
                startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dan_Chas.this, "xxxxx", Toast.LENGTH_SHORT).show();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.Dan_chas_layout,new Dan_rogbalais()).commit();

            }
        });




    }
}