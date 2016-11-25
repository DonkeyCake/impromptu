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
import com.promptu.event.events.TrackSelectedEvent;
import com.promptu.gl.GLLauncher;
import com.promptu.gl.display.skin.SkinManager;
import com.promptu.helpers.LambdaClassListener;
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
    TrackSelectedEvent trackSelectedEvent;

    public SidebarBody() {
        super(false);
        fileChooser = new FileChooser(FileChooser.Mode.OPEN);
        fileChooser.setDirectory(new File("./"));
        fileChooser.setMultiSelectionEnabled(false);
        trackSelectedEvent = new TrackSelectedEvent();
        openListener = new FileChooserAdapter() {
            @Override public void canceled() {}

            @Override
            public void selected(Array<FileHandle> files) {
                if(files.size <= 0) return;
                FileHandle first = files.first();
                try {
                    LocalDatabase.DataSet dataSet = SerializationManager.instance().fromFile(first.file().toPath(), LocalDatabase.DataSet.class);
                    trackSelectedEvent.setDataSet(dataSet);
                    trackSelectedEvent.fire(SidebarBody.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        saveListener = new FileChooserAdapter() {
            @Override public void canceled() {}

            @Override
            public void selected(Array<FileHandle> files) {
                if(files.items.length <= 0) return;
                FileHandle first = files.first();
                SerializationManager.instance().toFile(first.file().toPath(), GLLauncher.instance().dataSet);
            }
        };
        init();
    }

    private void init() {
        TextButton loadBtn = new TextButton("Load", SkinManager.instance().visX1);
        TextButton saveBtn = new TextButton("Save", SkinManager.instance().visX1);

        loadBtn.addListener(new LambdaClassListener(this::load));
        saveBtn.addListener(new LambdaClassListener(this::save));

        add(loadBtn).growX().pad(0, 2, 4, 2).row();
        add(saveBtn).growX().pad(0, 2, 4, 2).row();
    }


    public void load(InputEvent event, float x, float y) { load(); }
    public void load() {
        fileChooser.setMode(FileChooser.Mode.OPEN);
        fileChooser.setListener(openListener);
        getStage().addActor(fileChooser.fadeIn());
    }

    public void save(InputEvent event, float x, float y) { save(); }
    public void save() {
        fileChooser.setMode(FileChooser.Mode.SAVE);
        fileChooser.setListener(saveListener);
        getStage().addActor(fileChooser.fadeIn());
    }

}
