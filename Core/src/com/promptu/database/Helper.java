package com.promptu.database;

/**
 * Created by Guy on 18/11/2016.
 */
public class Helper {

    protected String uid;
    protected String text;

    public Helper(String uid, String text) {
        this.uid = uid;
        this.text = text;
    }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

}
