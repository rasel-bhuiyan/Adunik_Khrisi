package com.example.adunik_krishi;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
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

import com.example.adunik_krishi.screens.LoginActivity;
import com.example.adunik_krishi.screens.WeatherActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

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
                    case R.id.menu_market_products_price:
                        Intent intentMarketPrice = new Intent(getApplicationContext(),Market_Price.class);
                        startActivity(intentMarketPrice);

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
                        updateLoginInfo();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }

                return true;
            }
        });

    }

    private void updateLoginInfo() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("phone",null);
        editor.putString("password",null);
        editor.putBoolean("isLogin",false);
        editor.apply();

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
                .setMessage("আপনি কি এপ থেকে বের হতে চান?").setCancelable(false)
                .setPositiveButton("হ্যাঁ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.super.onBackPressed();
                    }
                }) .setNegativeButton("না", null).show();
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