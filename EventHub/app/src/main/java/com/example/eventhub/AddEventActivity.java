package com.example.eventhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity {
    Button submitButton;
    EditText nameTxt, venueTxt, descTxt, orgTxt;
    TextView timeTxt;
    CheckBox pastChkBox;
    DatabaseReference reff;
    String Time = "01-01-2001 00:00:00";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        ConstraintLayout constraintLayout = findViewById(R.id.constraint_ID);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5500);
        animationDrawable.start();

        String clubName = getIntent().getStringExtra("clubName");
        reff = FirebaseDatabase.getInstance().getReference();
        submitButton = findViewById(R.id.submitButton);
        nameTxt = findViewById(R.id.nameText);
        descTxt = findViewById(R.id.descText);
        venueTxt = findViewById(R.id.venueText);
        orgTxt = findViewById(R.id.organizerText);
        timeTxt = findViewById(R.id.timeText);


        Button button = findViewById(R.id.Date_Time);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a calendar instance to get the current date and time
                Calendar calendar = Calendar.getInstance();

                // Create a DatePickerDialog to allow the user to select the date
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Set the selected date on the calendar instance
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                // Create a TimePickerDialog to allow the user to select the time
                                TimePickerDialog timePickerDialog = new TimePickerDialog(AddEventActivity.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                // Set the selected time on the calendar instance
                                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                                calendar.set(Calendar.MINUTE, minute);

                                                // Format the selected date and time into a string using SimpleDateFormat
                                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                                                String formattedDateTime = sdf.format(calendar.getTime());

                                                Time = formattedDateTime;
                                                String finalTime = Time.substring(0, Time.length()-2) + "00";
                                                Time = finalTime;
                                                timeTxt.setText(finalTime);

                                                // Do something with the formatted date and time string
                                                Log.d("DateTime", finalTime);
                                            }
                                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                                timePickerDialog.show();
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });



        submitButton.setOnClickListener(view -> {
            String  name = nameTxt.getText().toString(),
                    desc = descTxt.getText().toString(),
                    venue = venueTxt.getText().toString(),
                    org = orgTxt.getText().toString(),
//                    time = timeTxt.getText().toString();
                    time = Time;
            reff.child("Clubs").child(clubName).push().setValue(new Event(name,time,venue,desc,org,false));
            reff.child("ClubsName").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    List<String> clubNames = new ArrayList<>(); // fetch club list
                    boolean hasClub = false;
                    for (DataSnapshot ds: snapshot.getChildren()) {
                        String c_name = ds.getValue(String.class);
                        if (c_name.equals(clubName)) {
                            hasClub = true;
                        }
                        clubNames.add(c_name);
                    }
                    if (!hasClub) {
                        clubNames.add(clubName);
                        reff.child("ClubsName").setValue(clubNames);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed");
                }
            });
        Toast.makeText(this, "Successfully added the event.", Toast.LENGTH_SHORT).show();
            onDestroy();
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        return ;
    }
}