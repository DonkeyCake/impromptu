package com.promptu.event.events;

import com.promptu.event.AbstractEvent;
import com.promptu.event.Subscribe;

/**
 * Created by Guy on 25/11/2016.
 */
public class TrackSelectedEvent extends AbstractEvent {

    public String trackPath = "";

    public TrackSelectedEvent() {}

    public TrackSelectedEvent(String trackPath) {
        this.trackPath = trackPath;
    }

    public String getTrackPath() {
        return trackPath;
    }

    public void setTrackPath(String trackPath) {
        this.trackPath = trackPath;
    }

    public static interface TrackSelectedListener {
        @Subscribe
        public void onTrackSelected(Object source, TrackSelectedEvent event);
    }

}
