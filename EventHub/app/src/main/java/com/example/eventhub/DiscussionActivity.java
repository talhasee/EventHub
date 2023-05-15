package com.example.eventhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DiscussionActivity extends AppCompatActivity {
    DatabaseReference reff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);
        LinearLayout parent = findViewById(R.id.discLayout);
        Intent intent = getIntent();
        String club_name = intent.getStringExtra("CLUB_NAME"), key = intent.getStringExtra("KEY");
        reff = FirebaseDatabase.getInstance().getReference()
                .child("Discussion");
        findViewById(R.id.submitCommentBtn).setOnClickListener(view -> {
            String title = ((EditText)findViewById(R.id.titleTxt)).getText().toString();
            String comment = ((EditText)findViewById(R.id.commentTxt)).getText().toString();
            reff.push().setValue(new DiscussionDataClass(LoginActivity.userEmail, title, comment));
        });
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<DiscussionDataClass> ddcLs = new ArrayList<>();
                    parent.removeAllViews();
                    for (DataSnapshot ds: snapshot.getChildren()) {
                        ddcLs.add(ds.getValue(DiscussionDataClass.class));
                    }
                    for (int i = 0; i < ddcLs.size(); i++) {
                        DiscussionDataClass ddc = ddcLs.get(i);
                        View view = getLayoutInflater().inflate(R.layout.comment_item, null);
                        RelativeLayout relativeLayout = view.findViewById(R.id.commentItem);
                        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
                        animationDrawable.setEnterFadeDuration(2500);
                        animationDrawable.setExitFadeDuration(5500);
                        animationDrawable.start();
                        ((TextView)view.findViewById(R.id.titleTxt)).setText(ddc.getTitle());
                        ((TextView)view.findViewById(R.id.discTxt)).setText(ddc.getTxt());
                        ((TextView)view.findViewById(R.id.emailTxt)).setText(ddc.getEmail());
                        parent.addView(view);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed");
            }
        });
    }
}