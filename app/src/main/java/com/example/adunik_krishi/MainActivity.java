package com.example.adunik_krishi;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.adunik_krishi.constant.Constant;
import com.example.adunik_krishi.screens.LoginActivity;
import com.example.adunik_krishi.screens.RegisterActivity;
import com.example.adunik_krishi.screens.WeatherActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.adunik_krishi.R.id.toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getLocationPermission();


        // navigation Drawer
        nav = findViewById(R.id.nav_menu);
        drawerLayout = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_home:
                        Toast.makeText(getApplicationContext(), "Home panel is open", Toast.LENGTH_SHORT).show();
                        // fragment load start
                        Home home = new Home();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.mainActivityLayout,home,"homeFragment");
                        fragmentTransaction.commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_chasabad:
                        Toast.makeText(getApplicationContext(), "this is chasabad " , Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.menu_instrument:
                        Toast.makeText(MainActivity.this, "call", Toast.LENGTH_SHORT).show();
                        krishi_instrument krishi_instrument = new krishi_instrument();
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.mainActivityLayout,krishi_instrument,"krishi instrument");
                        ft.commit();

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_marketing:
                        Toast.makeText(MainActivity.this, "Bazar jat koron ", Toast.LENGTH_SHORT).show();
                        BazarJatKoron bazarJatKoron = new BazarJatKoron();

                        FragmentManager fm2 = getSupportFragmentManager();
                        FragmentTransaction ft2 = fm2.beginTransaction();
                        ft2.replace(R.id.mainActivityLayout,bazarJatKoron,"bazarjatKoron");
                        ft2.commit();

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_weather:
                        startActivity(new Intent(MainActivity.this, WeatherActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_setting:
                        Toast.makeText(MainActivity.this, "setting", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }

                return true;
            }
        });

    }

    private void getLocationPermission() {

        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(permissions,1);
        }
    }

    // cancel dialog message shoow
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.super.onBackPressed();
                    }
                }) .setNegativeButton("No", null).show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Home home = new Home();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainActivityLayout,home,"homeFragment");
        fragmentTransaction.commit();
        drawerLayout.closeDrawer(GravityCompat.START);
    }
}