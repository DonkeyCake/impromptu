package com.promptu.database;

import com.promptu.utils.StringGenerator;

import java.util.Base64;
import java.util.Random;

/**
 * Created by Guy on 18/11/2016.
 */
public class MarkerPoint {

    protected String uid;
    protected String mbid;
    protected TimeUnit startTime;
    protected String colourHex;
    protected String header;
    protected String text;

    public MarkerPoint() {
        uid = StringGenerator.generateString();
        startTime = new TimeUnit();
        mbid("");
        colourHex("");
        header("Header");
        text("Text");
    }

    public String uid() {
        return this.uid;
    }

    public MarkerType getType() {
        return MarkerType.POINT;
    }

    public String mbid() {
        return mbid;
    }

    public TimeUnit startTime() {
        return startTime;
    }

    public String colourHex() {
        return colourHex;
    }

    public void mbid(String mbid) {
        this.mbid = mbid;
    }
    public void startTime(String str) {

    }
    public void colourHex(String hex) {
        this.colourHex = hex;
    }

    public void header(String header) {
        this.header = header;
    }
    public String header() {
        return this.header;
    }
    public void text(String text) {
        this.text = text;
    }
    public String text() {
        return this.text;
    }

}
