package com.promptu.database;

/**
 * Created by Guy on 18/11/2016.
 */
public class TimeUnit implements Comparable {

    // TODO establish standard time unit

    int tickIndex;

    public void increment() {
        tickIndex++;
    }

    public int tickIndex() {
        return tickIndex;
    }

    public void tickIndex(int index) {
        tickIndex = index;
    }

    @Override
    public String toString() {
        return ""+tickIndex;
    }

    @Override
    public int compareTo(Object o) {
        if(!(o instanceof TimeUnit)) return 0;
        TimeUnit unit = (TimeUnit)o;
        if(tickIndex() > unit.tickIndex()) return 1;
        if(tickIndex() < unit.tickIndex()) return -1;
        return 0;
    }
}
