package com.promptu.event.events;

import com.promptu.database.LocalDatabase;
import com.promptu.event.AbstractEvent;
import com.promptu.event.Subscribe;

/**
 * Created by Guy on 24/11/2016.
 */
public class ProjectSelectedEvent extends AbstractEvent {

    public LocalDatabase.DataSet dataSet;

    public ProjectSelectedEvent() { this(null); }
    public ProjectSelectedEvent(LocalDatabase.DataSet dataSet) { this.dataSet = dataSet; }

    public LocalDatabase.DataSet getDataSet() { return dataSet; }
    public void setDataSet(LocalDatabase.DataSet dataSet) { this.dataSet = dataSet; }

    public static interface ProjectSelectedListener {
        @Subscribe
        void onProjectSelected(Object source, ProjectSelectedEvent event);
    }

}
