package com.example.eventhub;

import java.util.ArrayList;

public class Event {
    private String organizer,name, time, venue, desc; // dd-MM-yyyy hh:mm:ss
    private Boolean hasHappened;

    public Event(){

    }

    public Event(String _name, String _time, String _venue, String _desc, String organizer, Boolean _hasHappened) {
        this.name = _name;
        this.time = _time;
        this.venue = _venue;
        this.desc = _desc;
        this.organizer = organizer;
        this.hasHappened = _hasHappened;
    }


    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Boolean getHasHappened() {
        return hasHappened;
    }

    public void setHasHappened(Boolean hasHappened) {
        this.hasHappened = hasHappened;
    }

}