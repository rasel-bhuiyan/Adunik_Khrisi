package com.example.adunik_krishi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


public class Dan_rogbalais extends Fragment {

        TextView danRogWebsiteTv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dan_rogbalais, container, false);

        danRogWebsiteTv = view.findViewById(R.id.dan_rog_website);
        danRogWebsiteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "এই জন্য আপনার ইন্টারনেট সংযোগ প্রয়োজন!!", Toast.LENGTH_SHORT).show();

            }
        });


   return view;
    }
}