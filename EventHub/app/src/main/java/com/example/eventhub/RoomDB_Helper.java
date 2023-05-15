package com.example.eventhub;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Calendar_DB.class}, version = 2)
public abstract class RoomDB_Helper extends RoomDatabase {
    public abstract CalendarDB_DAO eventDao();

    private static volatile RoomDB_Helper INSTANCE;

    public static RoomDB_Helper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDB_Helper.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, RoomDB_Helper.class, "event_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
