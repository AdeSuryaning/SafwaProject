package com.example.safwa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends AppCompatActivity {

    private int waktu_loading = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent main = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(main);
                finish();
//                Toast.makeText(SplashScreenActivity.this, "nnnn", Toast.LENGTH_SHORT).show();
            }
        }, waktu_loading);
    }
}