package com.promptu.database;

/**
 * Created by Guy on 18/11/2016.
 */
public class TimeUnit {

    // TODO establish standard time unit

    int tickIndex;

    public void increment() {
        tickIndex++;
    }

    @Override
    public String toString() {
        return ""+tickIndex;
    }
}
