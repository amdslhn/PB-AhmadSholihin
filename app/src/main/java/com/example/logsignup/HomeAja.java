package com.example.logsignup;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeAja extends AppCompatActivity {

    private LinearLayout navWallet,calcu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_aja);
        Switch themeSwitch = findViewById(R.id.themeSwitch);
        int nightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        themeSwitch.setChecked(nightMode == Configuration.UI_MODE_NIGHT_YES);

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            recreate();
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    return true;
                } else if (id == R.id.nav_profile) {
                    startActivity(new Intent(HomeAja.this, ProfileActivity.class));
                    return true;
                } else if (id == R.id.nav_settings) {
                    startActivity(new Intent(HomeAja.this, SettingsActivity.class));
                    return true;
                }
                return false;
            }
        });
        navWallet = findViewById(R.id.nav_wallet);
        navWallet.setOnClickListener(view ->{
            startActivity(new Intent(HomeAja.this, CatatanKeuangan.class));
        });
        calcu = findViewById(R.id.hitung);
        calcu.setOnClickListener(view -> {
            startActivity(new Intent(HomeAja.this, Calcu.class));
        });
    }
}
