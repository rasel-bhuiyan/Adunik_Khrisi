package com.example.adunik_krishi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.adunik_krishi.constant.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Home extends Fragment implements View.OnClickListener {
    public Home() {
        // Required empty public constructor
    }

    TextView time_date,tempTV;
    CardView chasabad, mosshoChas;
    String city = "";
    DecimalFormat decimalFormat = new DecimalFormat("#.##");



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View HomeView = inflater.inflate(R.layout.fragment_home, container, false);
        // time and date
        tempTV = HomeView.findViewById(R.id.tempTV);
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

        getLocation();

        return HomeView;
    }

    private void getLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        } else {

            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            try{
                String fullCity = getCityName(location.getLatitude(), location.getLongitude());
                city = fullCity.substring(0,fullCity.indexOf(' '));

                getWeatherDetails(city);
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try{
                        String fullCity = getCityName(location.getLatitude(), location.getLongitude());

                        city = fullCity.substring(0,fullCity.indexOf(' '));

                        getWeatherDetails(city);

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "Location Permission Denied!!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void getWeatherDetails(String Mycity) {

        String tempUrl = "";

        tempUrl = Constant.WEATHER_API + "?q="+Mycity+"&appid="+Constant.WEATHER_API_KEY;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.d("response",response);

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("description");
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp") - 273.15;
                    tempTV.setText("আজকের তাপমাত্রাঃ \n" + decimalFormat.format(temp) + " °C");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    private String getCityName(double lat, double lon) {

        String cityName = "";

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocation(lat, lon, 10);
            if (addressList.size() > 0) {
                for (Address address : addressList) {


                    if (address.getLocality() != null && address.getLocality().length() > 0) {
                        cityName = address.getLocality();
                        Log.d("after:",address.getLocality());
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
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