package com.promptu.event.events;

import com.promptu.database.LocalDatabase;
import com.promptu.event.AbstractEvent;
import com.promptu.event.Subscribe;

/**
 * Created by Guy on 24/11/2016.
 */
public class TrackSelectedEvent extends AbstractEvent {

    public LocalDatabase.DataSet dataSet;

    public TrackSelectedEvent() { this(null); }
    public TrackSelectedEvent(LocalDatabase.DataSet dataSet) { this.dataSet = dataSet; }

    public LocalDatabase.DataSet getDataSet() { return dataSet; }
    public void setDataSet(LocalDatabase.DataSet dataSet) { this.dataSet = dataSet; }

    public static interface TrackSelectedListener {
        @Subscribe
        void onTrackSelected(Object source, TrackSelectedEvent event);
    }

}
