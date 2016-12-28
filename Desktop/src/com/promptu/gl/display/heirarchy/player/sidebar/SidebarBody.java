package com.promptu.gl.display.heirarchy.player.sidebar;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.file.FileChooserListener;
import com.promptu.database.LocalDatabase;
import com.promptu.event.events.ProjectSelectedEvent;
import com.promptu.gl.display.skin.SkinManager;
import com.promptu.gl.events.OpenCLIEvent;
import com.promptu.helpers.LambdaClassListener;
import com.promptu.project.ProjectManager;
import com.promptu.serialization.SerializationManager;

import java.io.File;
import java.io.IOException;

/**
 * Created by Guy on 24/11/2016.
 */
public class SidebarBody extends VisTable {

    FileChooser fileChooser;
    FileChooserListener openListener;
    FileChooserListener saveListener;
    ProjectSelectedEvent projectSelectedEvent;

    public SidebarBody() {
        super(false);
        fileChooser = new FileChooser(FileChooser.Mode.OPEN);
        fileChooser.setDirectory(new File("./"));
        fileChooser.setMultiSelectionEnabled(false);
        projectSelectedEvent = new ProjectSelectedEvent();
        openListener = new FileChooserAdapter() {
            @Override public void canceled() {}

            @Override
            public void selected(Array<FileHandle> files) {
                if(files.size <= 0) return;
                FileHandle first = files.first();
                try {
                    LocalDatabase.DataSet dataSet = SerializationManager.instance().fromFile(first.file().toPath(), LocalDatabase.DataSet.class);
                    projectSelectedEvent.setDataSet(dataSet);
                    projectSelectedEvent.fire(SidebarBody.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        saveListener = new FileChooserAdapter() {
            @Override public void canceled() {}

            @Override
            public void selected(Array<FileHandle> files) {
                if(files.size <= 0) return;
                FileHandle first = files.first();
                LocalDatabase.DataSet set = ProjectManager.instance().dataSet;
                set.prepare();
                SerializationManager.instance().toFile(first.file().toPath(), set);
            }
        };
        init();
    }

    private void init() {

        TextButton newBtn = new TextButton("New project", SkinManager.instance().visX1);
        TextButton trackBtn = new TextButton("Select audio track", SkinManager.instance().visX1);

        TextButton loadBtn = new TextButton("Load project", SkinManager.instance().visX1);
        TextButton saveBtn = new TextButton("Save project", SkinManager.instance().visX1);

        TextButton cliBtn = new TextButton("Open CLI", SkinManager.instance().visX1);

        newBtn.addListener(new LambdaClassListener(this::newProject));
        trackBtn.addListener(new LambdaClassListener(this::selectTrack));
        loadBtn.addListener(new LambdaClassListener(this::load));
        saveBtn.addListener(new LambdaClassListener(this::save));
        cliBtn.addListener(new LambdaClassListener((event, x, y) -> new OpenCLIEvent().fire(this)));

        add(newBtn).growX().pad(0, 2, 4, 2).row();
        add(trackBtn).growX().pad(0, 2, 16, 2).row();
        add(loadBtn).growX().pad(0, 2, 4, 2).row();
        add(saveBtn).growX().pad(0, 2, 16, 2).row();
        add(cliBtn).growX().pad(0, 2, 4, 2).row();
    }


    public void load(InputEvent event, float x, float y) { load(); }
    public void load() {
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        fileChooser.setMode(FileChooser.Mode.OPEN);
        fileChooser.setListener(openListener);
        getStage().addActor(fileChooser.fadeIn());
    }

    public void save(InputEvent event, float x, float y) { save(); }
    public void save() {
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        fileChooser.setMode(FileChooser.Mode.SAVE);
        fileChooser.setListener(saveListener);
        getStage().addActor(fileChooser.fadeIn());
    }

    public void selectTrack(InputEvent event, float x, float y) { selectTrack(); }
    public void selectTrack() {
        getStage().addActor(ProjectManager.instance().getMusicTrackChooser().fadeIn());
    }

    public void newProject(InputEvent event, float x, float y) { newProject(); }
    public void newProject() {
        fileChooser.setSelectionMode(FileChooser.SelectionMode.DIRECTORIES);
        fileChooser.setMode(FileChooser.Mode.OPEN);
        fileChooser.setListener(new FileChooserAdapter(){
            @Override
            public void selected(Array<FileHandle> files) {
                if(files.size <= 0) return;
                FileHandle first = files.first();
                File f = first.file();
                ProjectManager pm = ProjectManager.instance();
                if(pm.projectPath.length() > 0 && pm.dataSet != null)
                    SerializationManager.instance().toFile(new File(pm.projectPath).toPath(), pm.dataSet);
                pm.projectPath = f.getPath();
                pm.dataSet = new LocalDatabase.DataSet();
            }
        });
        getStage().addActor(fileChooser.fadeIn());
    }

}
