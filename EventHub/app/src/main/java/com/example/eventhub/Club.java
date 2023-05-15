package com.example.eventhub;

import java.util.ArrayList;

public class Club {
    private String Cname;
    private ArrayList<Event> EList;

    public Club() {
    }

    public Club(String _Cname, ArrayList<Event> _EList) {
        this.Cname = _Cname;
        this.EList = _EList;
    }


    public String getCname() {
        return Cname;
    }

    public void setCname(String cname) {
        Cname = cname;
    }

    public ArrayList<Event> getEList() {
        return EList;
    }

    public void setEList(ArrayList<Event> EList) {
        this.EList = EList;
    }
}