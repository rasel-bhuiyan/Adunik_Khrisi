package com.example.adunik_krishi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BazarJatKoron#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BazarJatKoron extends Fragment {


    public BazarJatKoron() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View Bazarjatkoron =  inflater.inflate(R.layout.fragment_bazar_jat_koron, container, false);
        return Bazarjatkoron;
    }
}