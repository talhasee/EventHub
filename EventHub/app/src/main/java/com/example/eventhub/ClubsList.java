package com.example.eventhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ClubsList extends AppCompatActivity {

    Button ShowEvents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubs_list);

        ShowEvents = findViewById(R.id.ShowEvents);

//        ConstraintLayout constraintLayout = findViewById(R.id.ClubListID);
//        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
//        animationDrawable.setEnterFadeDuration(2500);
//        animationDrawable.setExitFadeDuration(5500);
//        animationDrawable.start();


        // Placing fragment.
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, new NonTechClubsFragmentList());
        TextView textView = findViewById(R.id.clubListHeading);
        textView.setText("Non-Technical Clubs");
        fragmentTransaction.commit();

        ShowEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClubsList.this, Calendar_ListView.class);
                startActivity(intent);
            }
        });
    }

    public void nonTechButton(View view){
        NonTechClubsFragmentList nonTechClubsFragmentList = new NonTechClubsFragmentList();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, nonTechClubsFragmentList);
        TextView textView = findViewById(R.id.clubListHeading);
        textView.setText("Non-Technical Clubs");
        fragmentTransaction.commit();
    }

    public void techButton(View view){
        TechClubsListFragment techClubsListFragment = new TechClubsListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,techClubsListFragment);
        TextView textView = findViewById(R.id.clubListHeading);
        textView.setText("Technical Clubs");
        fragmentTransaction.commit();
    }
}