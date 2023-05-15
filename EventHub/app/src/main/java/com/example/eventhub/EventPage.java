package com.example.eventhub;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;
import android.Manifest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EventPage extends AppCompatActivity {

    private static final int REQUEST_CODE_SET_ALARM_PERMISSION = 1;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 1;
    RadioGroup radioGroup;
    RadioButton Before10, Before20, Custom;
    Button button;
    String event_name, organizer, desc, dateString, venue, location;

    private static final int ADD_EVENT_REQUEST_CODE = 1;
    private Uri eventUri;
//    Location need to be added as field while taking event data
    int time = 0, h_, m_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        Button Reminder_Notification = findViewById(R.id.Reminder_Notification);
        Button ShowEvents = findViewById(R.id.ShowEvents);

        ConstraintLayout constraintLayout = findViewById(R.id.eventPageID);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5500);
        animationDrawable.start();

        // Fetching Events detail from prev component.
        Intent intent = getIntent();
        event_name = intent.getStringExtra("EVENT");
        organizer = intent.getStringExtra("CLUB_NAME");
        desc = intent.getStringExtra("DESCRIPTION");
        dateString = intent.getStringExtra("TIME");
        venue = intent.getStringExtra("VENUE");


        String description = "ORGANIZER: " + organizer + "\n"  + "DATE: " + dateString + "\nVENUE: " + venue ;
        description += "\n" + desc;

//        Toast.makeText(this, event_name +" " + organizer +" " + desc + " " + dateString + " " + venue,Toast.LENGTH_SHORT).show();
        String descriptionString = "ORGANIZER: " + organizer + "\n"  + "DATE: " + dateString + "\nVENUE: " + venue ;
        descriptionString += "\n" + desc;
//        Toast.makeText(this, descriptionString, Toast.LENGTH_SHORT).show();


        // Set text in views.
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView eventName = findViewById(R.id.eventName);
        eventName.setTypeface(null, Typeface.BOLD_ITALIC);
        eventName.setText(event_name);

        TextView descriptionText = findViewById(R.id.description);
        descriptionText.setText(descriptionString);

        Reminder_Notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(EventPage.this, Manifest.permission.SET_ALARM)
                        == PackageManager.PERMISSION_DENIED) {
                    // Permission is not granted, request it
                    ActivityCompat.requestPermissions(EventPage.this, new String[]{Manifest.permission.SET_ALARM},
                            REQUEST_CODE_SET_ALARM_PERMISSION);
                    Log.d("REMINDER_SET_00", "ASKING FOR PERMISSION");
                } else {
                    // Permission already granted, proceed with setting the reminder
                    try {
                        showPopupWindow(Reminder_Notification);
                    } catch (java.text.ParseException e) {
                        throw new RuntimeException(e);
                    }
//                    setReminder(1, "REMINDER CHECK");
                }
            }
        });


        ShowEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Show_Reminders_List();
            }
        });

        findViewById(R.id.showDescButton).setOnClickListener(view -> {
            Intent i = new Intent(this, DiscussionActivity.class);
            i.putExtra("CLUB_NAME", intent.getStringExtra("CLUB_NAME"));
            i.putExtra("KEY", intent.getStringExtra("KEY"));
            startActivity(i);
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_SET_ALARM_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with setting the reminder
            } else {
                // Permission denied, show a message or disable the feature that requires this permission
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void AddCalendarEvent(View view, int h, int m){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        int hh = 0, mm = 0, dd = 0, MM = 0, yyyy = 0;
        int hh1 = 0, mm1 = 0, dd1 = 0, MM1 = 0, yyyy1 = 0;
        try {
            Date date = format.parse(dateString);
            h *= -1;
            m *= -1;
            Log.d("REMINDER_TIME", "Firebase Date "+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date));

            Log.d("REMINDER_TIME", "Input h, m : " + Integer.toString(h) +" : "+ Integer.toString(m));

//            long timeInMillis = date.getTime();
//            timeInMillis -= m * 60 * 1000;
//            date.setTime(timeInMillis);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            calendar.add(Calendar.HOUR_OF_DAY, h);
            calendar.add(Calendar.MINUTE, m);
            calendar.add(Calendar.MONTH, -1);
            Date updatedDate = calendar.getTime();

            Log.d("REMINDER_TIME", "Minus Date "+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(updatedDate));

            hh = Integer.parseInt(new SimpleDateFormat("HH").format(updatedDate));
            mm = Integer.parseInt(new SimpleDateFormat("mm").format(updatedDate));
            dd = Integer.parseInt(new SimpleDateFormat("dd").format(updatedDate));
            MM = Integer.parseInt(new SimpleDateFormat("MM").format(updatedDate));
            yyyy = Integer.parseInt(new SimpleDateFormat("yyyy").format(updatedDate));


            Log.d("REMINDER_TIME", "Start "+Integer.toString(hh)+ " "+ Integer.toString(mm)+ " "+ Integer.toString(dd)+ " "+ Integer.toString(MM)+ " "+ Integer.toString(yyyy));

            Date newDate = format.parse(dateString);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(newDate);

            calendar2.add(Calendar.HOUR_OF_DAY, 1);
            calendar2.add(Calendar.MONTH, -1);

            Date updatedNewDate = calendar2.getTime();

            Log.d("REMINDER_TIME", "End Time "+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(updatedNewDate));

            hh1 = Integer.parseInt(new SimpleDateFormat("HH").format(updatedNewDate));
            mm1 = Integer.parseInt(new SimpleDateFormat("mm").format(updatedNewDate));
            dd1 = Integer.parseInt(new SimpleDateFormat("dd").format(updatedNewDate));
            MM1 = Integer.parseInt(new SimpleDateFormat("MM").format(updatedNewDate));
            yyyy1 = Integer.parseInt(new SimpleDateFormat("yyyy").format(updatedNewDate));
            Log.d("REMINDER_TIME", Integer.toString(hh1)+ " "+ Integer.toString(mm1)+ " "+ Integer.toString(dd1)+ " "+ Integer.toString(MM1)+ " "+ Integer.toString(yyyy1));

        } catch (ParseException | java.text.ParseException e) {
            e.printStackTrace();
        }
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(yyyy, MM, dd, hh, mm); //Get Timings from Firebase
        Calendar endTime = Calendar.getInstance();
        endTime.set(yyyy1, MM1, dd1, hh1, mm1);

        String title = event_name+"_eh"; // Added '_eh' denoting our event
        String description = desc;
        String location = "IIIT Delhi, Okhla Industrial Estate, Phase III, near Govind Puri Metro Station, New Delhi, Delhi 110020";

        Intent intent = new Intent(Intent.ACTION_EDIT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.DESCRIPTION, description)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .putExtra("created_by_app", true);

        startActivityForResult(intent, ADD_EVENT_REQUEST_CODE);
        //Database Update
        Calendar_DB event = new Calendar_DB(title, description, location, beginTime.getTimeInMillis(), endTime.getTimeInMillis()); //, eventUri.toString());
        RoomDB_Helper.getInstance(this).eventDao().insert(event);
//        Log.d("REMINDER_URI", "URI-----> "+eventUri.toString());
        long e_id = getEventID(title);
        Log.d("REMINDER_EVENT", Integer.toString((int)e_id));
//        Utility utility = new Utility(this);
//        ArrayList<String> ev = new ArrayList<String>();
//        utility.readCalendarEvent(EventPage.this);
//        ev = utility.nameOfEvent;
//        Log.d("UTILITY_OBJ", ev.toString());
    }

    public long getEventID(String eventTitle) {
        long eventID = -1; // default value if event is not found

        String[] projection = new String[]{CalendarContract.Events._ID};
        String selection = CalendarContract.Events.TITLE + " = ?";
        String[] selectionArgs = new String[]{eventTitle};

        Cursor cursor = getContentResolver().query(
                CalendarContract.Events.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            int temp = cursor.getColumnIndex(CalendarContract.Events._ID);
            if(temp > 0)
                eventID = cursor.getLong(temp);
            cursor.close();
        }

        return eventID;
    }

    public void RemoveCalendarEvent(View view) {
        String[] projection = new String[]{CalendarContract.Events._ID, CalendarContract.Events.TITLE};
        String selection = CalendarContract.Events.TITLE + " = ?";
        String[] selectionArgs = new String[]{event_name};

        Cursor cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, projection, selection, selectionArgs, null);

        if (cursor.moveToFirst()) {
            int eventId = -1, temp = cursor.getColumnIndex(CalendarContract.Events._ID);
            if(cursor.getColumnIndex(CalendarContract.Events._ID) > 0) {
                eventId = cursor.getInt(temp);
                Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId);
                int rows = getContentResolver().delete(deleteUri, null, null);

                if (rows > 0) {
                    Toast.makeText(this, "Event removed successfully", Toast.LENGTH_SHORT).show();
                }
            }
            else
                Toast.makeText(this, "Event Not Found!!!", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }

    public void showPopupWindow(View view) throws java.text.ParseException {
        // Inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_reminder, null);
        Calendar currentTime = Calendar.getInstance();

        //Get Event Timings here....To be changed
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        Date date = format.parse(dateString);

        Log.d("REMINDER_TIME", dateString);
        int hour = Integer.parseInt(new SimpleDateFormat("hh").format(date));
        int minute = Integer.parseInt(new SimpleDateFormat("mm").format(date));

        radioGroup = popupView.findViewById(R.id.radioGroup);
        Before10 = popupView.findViewById(R.id.Before10);
        Before20 = popupView.findViewById(R.id.Before20);
        Custom = popupView.findViewById(R.id.Custom);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == Before10.getId()){
//                    setReminder(10, "Event-10"); //changes to be made ....Getting Event time, Event name
                    h_ = 0;
                    m_ = 10;
                    time = 10;
                }
                else if(checkedId == Before20.getId()){
//                    setReminder(20, "Event-20");
                    h_ = 0;
                    m_ = 20;
                    time = 20;
                }

                else if(checkedId == Custom.getId()){
                    // Show the time picker
                    TimePickerDialog timePickerDialog = new TimePickerDialog(EventPage.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
                            // Calculate the difference between the selected time and event time in minutes
                            h_ = Math.abs(hour - hourOfDay);
                            m_ = Math.abs(minute - minutes);

                            String time1 = Integer.toString(hour) + ":" + Integer.toString(minute);
                            String time2 = Integer.toString(hourOfDay) + ":" + Integer.toString(minutes);
                            Log.d("REMINDER_TIME", "time1: "+ time1 + " time2: "+time2);
                            // Convert the times to minutes
                            int minutes1 = Integer.parseInt(time1.split(":")[0]) * 60 + Integer.parseInt(time1.split(":")[1]);
                            int minutes2 = Integer.parseInt(time2.split(":")[0]) * 60 + Integer.parseInt(time2.split(":")[1]);

                            // Subtract the times in minutes
                            int diffMinutes = Math.abs(minutes1 - minutes2);

                            // Convert the result back to "hh:mm" format
                            int hours_ = diffMinutes / 60;
                            int minutes_ = diffMinutes % 60;
                            String diffTime = String.format("%02d:%02d", hours_, minutes_);


                            Log.d("REMINDER_TIME", "Difference : "+ diffTime);

                            String[] timeparts = diffTime.split(":");
                            h_ = Integer.parseInt(timeparts[0]);
                            m_ = Integer.parseInt(timeparts[1]);
                            time = (h_ * 60) + m_;
//                            time /= 2;
                            Log.d("REMINDER_TIME", "hour: "+ h_+" minutes: "+m_);
                        }
                    }, hour, minute, false);
                    timePickerDialog.show();
                }
            }
        });
        // Create the popup window
        int width = 950;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // Let taps outside the popup also dismiss it
        PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // Show the popup window
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // Dismiss the popup window when the close button is clicked
        Button buttonClose = popupView.findViewById(R.id.buttonClose);
        buttonClose.setOnClickListener(v -> {
            popupWindow.dismiss();
        });

        // Handle setting the reminder when the "Set Reminder" button is clicked
        Button buttonSetReminder = popupView.findViewById(R.id.buttonSetReminder);

        buttonSetReminder.setOnClickListener(v -> {
            AddCalendarEvent(v, h_, m_);
            // Dismiss the popup window
            popupWindow.dismiss();
        });
    }


    public void DeleteCalendarEvent(View view){
        // Assuming you have the URI stored in a variable called 'eventUri'
        Uri eventUri = Uri.parse("content://com.android.calendar/events/12345"); // Replace with the actual URI

        // Delete the event with the given URI
        ContentResolver cr = getContentResolver();
        int rowsDeleted = cr.delete(eventUri, null, null);

        if (rowsDeleted > 0) {
            // Event successfully deleted
            Toast.makeText(this, "Event deleted from calendar", Toast.LENGTH_SHORT).show();
        } else {
            // No event found with the given URI
            Toast.makeText(this, "Event not found in calendar", Toast.LENGTH_SHORT).show();
        }
    }
    public void Del(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
                == PackageManager.PERMISSION_DENIED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_CALENDAR)) {
                // show a rationale message explaining why the app needs this permission
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Permission Required");
                builder.setMessage("This app requires the WRITE_CALENDAR permission to set reminders.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(EventPage.this,
                                new String[]{Manifest.permission.WRITE_CALENDAR},
                                MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
                    }
                });
                builder.show();
            } else {
                // request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_CALENDAR},
                        MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
            }
        }
//        Utility utility = new Utility(this);
//        utility.deleteEventByTitle(EventPage.this, "Fashion Studio");
    }

    public void Show_Reminders_List(){
        Intent intent = new Intent(this, Calendar_ListView.class);
        startActivity(intent);
    }

}