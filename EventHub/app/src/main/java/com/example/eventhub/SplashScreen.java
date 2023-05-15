package com.example.eventhub;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private boolean shouldLaunchMain = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        shouldLaunchMain = true;

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                launchMainActivity();
            }
        },2100);
    }
    private void launchMainActivity() {
        if (shouldLaunchMain) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        shouldLaunchMain = false;
        super.onBackPressed();
    }
}
