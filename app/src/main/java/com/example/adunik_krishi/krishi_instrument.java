package com.example.adunik_krishi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class krishi_instrument extends Fragment {


    public krishi_instrument() {
        // Required empty public constructor
    }

    // back button on toolbar




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_krishi_instrument, container, false);
        return v;
    }
}