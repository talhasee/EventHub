package com.example.eventhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ClubPage extends AppCompatActivity {

    String clubName;
    Button ShowEvents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_page);
        // Setting the image and the club name.
        Intent intent = getIntent();
        clubName = intent.getStringExtra("CLUB_NAME");
        TextView textView = findViewById(R.id.clubPageHeading);
        textView.setText(clubName);
        textView.setTypeface(null, Typeface.BOLD_ITALIC);

        ShowEvents = findViewById(R.id.ShowEvents);

        String imageName = clubName.toLowerCase() ;

        @SuppressLint("DiscouragedApi") int resourceId = this.getResources().getIdentifier(imageName, "drawable", this.getPackageName());
        ImageView imageView = findViewById(R.id.clubImage);
        imageView.setImageResource(resourceId);
        // Pass the string name as per the club first name
//        imageView.setImageResource(R.drawable.cyborg);
//        Bitmap bMap = BitmapFactory.decodeFile("app\\src\\main\\res\\drawable\\cyborg.jpg");
//        imageView.setImageBitmap(bMap);
        // Placing fragment.
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, new UpcomingEventsListFragment());
        fragmentTransaction.commit();
        Button button = findViewById(R.id.upcomingEvents);
        button.setVisibility(View.GONE);


        ShowEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(ClubPage.this, Calendar_ListView.class);
                startActivity(intent);
            }
        });
    }

    public void moveToUpcomingEvents(View view){
        UpcomingEventsListFragment upcomingEventsListFragment = new UpcomingEventsListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, upcomingEventsListFragment);
        fragmentTransaction.commit();
        Button button = findViewById(R.id.upcomingEvents);
        button.setVisibility(View.GONE);
        button = findViewById(R.id.pastEvents);
        button.setVisibility(View.VISIBLE);
    }

    public String getClubName(){
        return clubName;
    }

    public void moveToPastEvents(View view){
        PastEventsListFragments pastEventsListFragments = new PastEventsListFragments();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, pastEventsListFragments);
        fragmentTransaction.commit();
        Button button = findViewById(R.id.upcomingEvents);
        button.setVisibility(View.VISIBLE);
        button = findViewById(R.id.pastEvents);
        button.setVisibility(View.GONE);
    }


}