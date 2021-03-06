package com.promptu.database;

/**
 * Created by Guy on 18/11/2016.
 */
public class MarkerBlock extends MarkerPoint {

    protected TimeUnit endTime;

    public MarkerBlock() {
        super();
        endTime = new TimeUnit();
    }

    public TimeUnit endTime() {
        return endTime;
    }

    public void endTime(String str) {
        try{
            endTime.tickIndex(Float.parseFloat(str));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MarkerType getType() {
        return MarkerType.BLOCK;
    }

}