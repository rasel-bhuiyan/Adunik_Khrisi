package com.example.adunik_krishi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Home extends Fragment implements View.OnClickListener {
    public Home() {
        // Required empty public constructor
    }

    TextView time_date;
    CardView chasabad, mosshoChas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View HomeView = inflater.inflate(R.layout.fragment_home, container, false);
        // time and date
        time_date = HomeView.findViewById(R.id.time);
        SimpleDateFormat TimeFromat = new SimpleDateFormat("HH:mm:ss:aa");
        SimpleDateFormat DateFromat = new SimpleDateFormat("dd:MM:yyyy");
        String dateTime = TimeFromat.format(new Date()).toString() + "\n" + DateFromat.format(new Date());
        time_date.setText(dateTime);

        //
        chasabad = HomeView.findViewById(R.id.H_chasabad);
        mosshoChas = HomeView.findViewById(R.id.h_fishChash);

        chasabad.setOnClickListener(this);
        mosshoChas.setOnClickListener(this);


        return HomeView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.H_chasabad:
                Toast.makeText(getActivity(), "chasabad", Toast.LENGTH_SHORT).show();
                Intent intentChasha = new Intent(getActivity().getApplication(),ChasabadPoddoti.class);
                startActivity(intentChasha);

              break;
            case R.id.h_fishChash:
                Intent intentFishchasabadPoddoti = new Intent(getActivity().getApplication(),FishCahsabad.class);
                startActivity(intentFishchasabadPoddoti);
                break;
        }

    }
}