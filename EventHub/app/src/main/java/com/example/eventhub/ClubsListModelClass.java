package com.example.eventhub;

public class ClubsListModelClass {
    private String textView1;
    private String divider;

    public ClubsListModelClass(String textView1, String divider){
        this.textView1 = textView1;
        this.divider = divider;
    }

    public String getDivider() {
        return divider;
    }

    public void setDivider(String divider) {
        this.divider = divider;
    }


    public String getTextView1() {
        return textView1;
    }

    public void setTextView1(String textView1) {
        this.textView1 = textView1;
    }
}
