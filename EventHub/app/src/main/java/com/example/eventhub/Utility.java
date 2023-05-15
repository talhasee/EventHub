package com.example.eventhub;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Utility {
    public ArrayList<String> nameOfEvent = new ArrayList<String>();

    public ArrayList<String> startDates = new ArrayList<String>();
    public ArrayList<String> endDates = new ArrayList<String>();
    public ArrayList<String> descriptions = new ArrayList<String>();
    public ArrayList<String> eventIds = new ArrayList<String>();
    ContentResolver resolver;
    public Utility(Context context){
        resolver = context.getContentResolver();
    }

    public ArrayList<String> readCalendarEvent(Context context) {

        Cursor cursor = resolver
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[]{"calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation"}, null,
                        null, null);
        cursor.moveToFirst();
        // fetching calendars name
        String CNames[] = new String[cursor.getCount()];

        // fetching calendars id
        nameOfEvent.clear();
        startDates.clear();
        endDates.clear();
        descriptions.clear();
        Log.d("UTILITY_OBJ", "SIZE: "+ Integer.toString(cursor.getCount()));

        for (int i = 0; i < CNames.length; i++) {
            String str1="", str2, str3;
//            ArrayList<String> temp = new ArrayList<String>(3);
            nameOfEvent.add(cursor.getString(1));

            if(cursor.getString(3) != null){
                startDates.add(getDate(Long.parseLong(cursor.getString(3))));

            }
            if(cursor.getString(4) != null){
                endDates.add(getDate(Long.parseLong(cursor.getString(4))));
            }
            descriptions.add(cursor.getString(2));
            CNames[i] = cursor.getString(1);
            cursor.moveToNext();
        }
        return nameOfEvent;
    }
    public void deleteEventByTitle(Context context, String title) {
//        ContentResolver cr = context.getContentResolver();

        Cursor cursor = resolver.query(
                Uri.parse("content://com.android.calendar/events"),
                new String[]{"_id"},
                "title=?",
                new String[]{title},
                null);

        if (cursor.moveToFirst()) {
            int temp = cursor.getColumnIndex("_id");
            long id;
            if(temp >= 0) {
                id = cursor.getLong(temp);
                Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, id);
//                int rows = cr.delete(deleteUri, null, null);
                int rows = resolver.delete(deleteUri, null, null);

                Log.d("UTILITY_OBJ", "Rows deleted: " + rows);
                Toast.makeText(context, "Event Successfully Deleted", Toast.LENGTH_SHORT).show();

            }
            else {
                Log.d("UTILITY_OBJ", "EVENT ID NOT FOUND");
                Toast.makeText(context, "Event NOT FOUND!!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Log.d("UTILITY_OBJ", "EVENT ID NOT FOUND");
            Toast.makeText(context, "Event NOT FOUND!!", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }

    public String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
    public ArrayList<String> readCalendarEvent2(Context context) {
        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://com.android.calendar/events"),
                new String[]{"_id", "calendar_id", "title", "description",
                        "dtstart", "dtend", "eventLocation"}, null, null, null);
        cursor.moveToFirst();
        // fetching calendars name
        String CNames[] = new String[cursor.getCount()];

        // fetching calendars id
        nameOfEvent.clear();
        startDates.clear();
        endDates.clear();
        descriptions.clear();
        eventIds.clear(); // List to store event IDs
        Log.d("UTILITY_OBJ", "SIZE: "+ Integer.toString(cursor.getCount()));

        for (int i = 0; i < CNames.length; i++) {
            eventIds.add(cursor.getString(0)); // Add event ID to list
            nameOfEvent.add(cursor.getString(2));
            if(cursor.getString(4) != null){
                startDates.add(getDate(Long.parseLong(cursor.getString(4))));
            }
            if(cursor.getString(5) != null){
                endDates.add(getDate(Long.parseLong(cursor.getString(5))));
            }
            descriptions.add(cursor.getString(3));
            CNames[i] = cursor.getString(2);
            cursor.moveToNext();
        }
        return nameOfEvent;
    }

}
