package com.example.adunik_krishi;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import static com.example.adunik_krishi.R.id.toolbar;

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
                        Toast.makeText(MainActivity.this, "Weather", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_setting:
                        Toast.makeText(MainActivity.this, "setting", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }

                return true;
            }
        });
    }
}