package com.promptu.database;

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

    public MarkerPoint() {
        uid = Base64.getEncoder().encodeToString((new Random().nextInt()+"").getBytes());
        startTime = new TimeUnit();
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

}
