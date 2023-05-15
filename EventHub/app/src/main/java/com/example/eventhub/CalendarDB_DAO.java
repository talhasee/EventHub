package com.example.eventhub;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CalendarDB_DAO {
    @Query("SELECT * FROM Events ORDER BY start_time ASC")
    List<Calendar_DB> getAllEvents();

    @Query("SELECT DISTINCT title, * FROM Events ")
    List<Calendar_DB> getEventsList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Calendar_DB event);

    @Delete
    void delete(Calendar_DB event);
}
