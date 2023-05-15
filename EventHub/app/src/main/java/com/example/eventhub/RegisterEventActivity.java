package com.example.eventhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.RelativeLayout;

public class RegisterEventActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_event);

        ConstraintLayout constraintLayout = findViewById(R.id.constraintID);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5500);
        animationDrawable.start();

        findViewById(R.id.registerEventButton).setOnClickListener(view -> {
            Intent i = new Intent(this,AddEventActivity.class);
            i.putExtra("clubName", getIntent().getStringExtra("clubName"));
            startActivity(i);
        });
    }
}