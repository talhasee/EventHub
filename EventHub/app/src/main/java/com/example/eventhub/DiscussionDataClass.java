package com.example.eventhub;

import java.io.Serializable;

public class DiscussionDataClass {
    private String email;
    private String title;
    private String txt;

    public DiscussionDataClass() {

    }

    public DiscussionDataClass(String email, String title, String txt) {
        this.email = email;
        this.txt = txt;
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
