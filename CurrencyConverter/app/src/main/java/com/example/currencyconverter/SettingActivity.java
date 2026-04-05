package com.example.currencyconverter;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.ImageButton;
import android.widget.Switch;


public class SettingActivity extends AppCompatActivity {
    Switch darkModeSwitch;
    ImageButton prevBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        darkModeSwitch = findViewById(R.id.darkModeSwitch);
        prevBtn = findViewById(R.id.prevBtn);

        prevBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

       int mode = AppCompatDelegate.getDefaultNightMode();

       if (mode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
            darkModeSwitch.setChecked(true);
        }


        switchThemeMode();

    }

    private void switchThemeMode(){
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }
}