package com.promptu.gl;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.VisUI;
import com.promptu.database.LocalDatabase;
import com.promptu.event.EventBus;
import com.promptu.event.events.TrackSelectedEvent;
import com.promptu.gl.assets.Assets;
import com.promptu.gl.display.PlayerScreen;
import com.promptu.utils.ClasspathUtils;

/**
 * Created by Guy on 24/11/2016.
 */
public class GLLauncher extends Game implements TrackSelectedEvent.TrackSelectedListener {

    private static GLLauncher instance;
    public static GLLauncher instance() {
        return instance;
    }

    public LocalDatabase.DataSet dataSet;

    @Override
    public void create() {
        instance = this;
        dataSet = new LocalDatabase.DataSet();
        EventBus.getInstance().register(this);
        VisUI.load(Gdx.files.classpath(ClasspathUtils.classPathToFilePath(Assets.class)+"/VisUI/uiskin.json"));
        setScreen(new PlayerScreen());
    }

    @Override
    public void onTrackSelected(Object source, TrackSelectedEvent event) {
        if(source == this) return;
        this.dataSet = event.dataSet;
    }
}
