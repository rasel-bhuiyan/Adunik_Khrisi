package com.example.adunik_krishi.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.adunik_krishi.R;
import com.example.adunik_krishi.constant.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity {

    TextView cityTV, tempTV, feelTV, humidityTV, airTV, cloudTV, pressureTV, detailsTV, maxTempTV, minTempTV;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    String city;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        initialize();

        getLocation();
    }

    private void getLocation() {


        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        } else {

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            try{
                String fullCity = getCityName(location.getLatitude(), location.getLongitude());
                city = fullCity.substring(0,fullCity.indexOf(' '));

                if(city.isEmpty()){
                    getWeatherDetails("Savar");
                }
                else{
                    getWeatherDetails(city);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    private String getCityName(double lat, double lon) {

        String cityName = "";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try{
                        String fullCity = getCityName(location.getLatitude(), location.getLongitude());

                        city = fullCity.substring(0,fullCity.indexOf(' '));

                        if(city.isEmpty()){
                            getWeatherDetails("Savar");
                        }
                        else{
                            getWeatherDetails(city);
                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Location Permission Denied!!", Toast.LENGTH_SHORT).show();
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
                    double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                    double minTemp = jsonObjectMain.getDouble("temp_min") - 273.15;
                    double maxTemp = jsonObjectMain.getDouble("temp_max") - 273.15;
                    float pressure = jsonObjectMain.getInt("pressure");
                    int humidity = jsonObjectMain.getInt("humidity");
                    JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                    String wind = jsonObjectWind.getString("speed");
                    JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                    String clouds = jsonObjectClouds.getString("all");
                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                    String countryName = jsonObjectSys.getString("country");
                    String cityName = jsonResponse.getString("name");
                    cityTV.setText(cityName);
                    tempTV.setText("তাপমাত্রাঃ " + decimalFormat.format(temp) + " °C");
                    feelTV.setText("মনে হচ্ছেঃ "+ decimalFormat.format(feelsLike) + " °C");
                    maxTempTV.setText("সর্বোচ্চ তাপমাত্রাঃ "+decimalFormat.format(maxTemp) + " °C");
                    minTempTV.setText("সর্বনিম্ন তাপমাত্রাঃ "+decimalFormat.format(minTemp) + " °C");
                    humidityTV.setText("আদ্রতাঃ " + humidity + "%");
                    detailsTV.setText("মন্তব্যঃ "+ description);
                    airTV.setText("বাতাসের গতিঃ "+ wind + "m/s");
                    cloudTV.setText("মেঘের পরিমানঃ "+ clouds + "%");
                    pressureTV.setText("প্রেসারঃ"+ pressure + " hPa");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void initialize() {

        cityTV = findViewById(R.id.cityTV);
        tempTV = findViewById(R.id.tempTV);
        feelTV = findViewById(R.id.feelsTV);
        humidityTV = findViewById(R.id.humidityTV);
        detailsTV = findViewById(R.id.detailsTV);
        airTV = findViewById(R.id.airSpeedTV);
        cloudTV = findViewById(R.id.cloudTV);
        pressureTV = findViewById(R.id.pressureTV);
        maxTempTV = findViewById(R.id.maxTempTV);
        minTempTV = findViewById(R.id.minTempTV);
    }
}