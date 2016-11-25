package com.promptu.project;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.file.FileChooserListener;
import com.promptu.database.LocalDatabase;
import com.promptu.event.EventBus;
import com.promptu.event.events.ProjectSelectedEvent;
import com.promptu.event.events.TrackSelectedEvent;

import java.io.File;

/**
 * Created by Guy on 25/11/2016.
 */
public class ProjectManager implements ProjectSelectedEvent.ProjectSelectedListener, TrackSelectedEvent.TrackSelectedListener {

    private FileChooser musicTrackChooser;
    private FileChooserListener openListener;

    private static ProjectManager instance;
    public static ProjectManager instance() {
        if(instance == null) instance = new ProjectManager();
        return instance;
    }

    public String projectPath = "";
    public LocalDatabase.DataSet dataSet;

    private ProjectManager() {
        EventBus.getInstance().register(this);
        musicTrackChooser = new FileChooser(FileChooser.Mode.OPEN);
        musicTrackChooser.setMultiSelectionEnabled(false);
        musicTrackChooser.setDirectory(new File("./"));
        openListener = new FileChooserAdapter(){
            @Override
            public void selected(Array<FileHandle> files) {
                if(files.size <= 0) return;
                FileHandle first = files.first();
                File file = first.file();
                String path = file.getPath();
                System.out.println(path);
                dataSet.setTrackPath(path);
                new TrackSelectedEvent(path).fire(this);
            }
        };
        musicTrackChooser.setListener(openListener);
        dataSet = new LocalDatabase.DataSet();
        new ProjectSelectedEvent(dataSet).fire(this);
    }

    @Override
    public void onProjectSelected(Object source, ProjectSelectedEvent event) {
        if(source == this) return;
        if(event.dataSet == null) return;
        this.dataSet = event.dataSet;
    }

    public FileChooser getMusicTrackChooser() { return musicTrackChooser; }

    @Override
    public void onTrackSelected(Object source, TrackSelectedEvent event) {
        if(source == null) return;
        this.dataSet.setTrackPath(event.trackPath);
    }
}
