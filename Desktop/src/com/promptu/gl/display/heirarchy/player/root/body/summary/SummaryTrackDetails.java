package com.promptu.gl.display.heirarchy.player.root.body.summary;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.promptu.database.LocalDatabase;
import com.promptu.event.EventBus;
import com.promptu.event.events.ProjectSelectedEvent;
import com.promptu.project.ProjectManager;

/**
 * Created by Guy on 24/11/2016.
 */
public class SummaryTrackDetails extends VisTable implements ProjectSelectedEvent.ProjectSelectedListener {

    VisTextField trackLbl;
    VisTextField artistLbl;

    public SummaryTrackDetails() {
        super(false);
        EventBus.getInstance().register(this);
        init();
    }

    private void init() {
        trackLbl = new VisTextField("");
        artistLbl = new VisTextField("");

        trackLbl.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                ProjectManager.instance().dataSet.setTrackName(trackLbl.getText().toString());
            }
        });
        artistLbl.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                ProjectManager.instance().dataSet.setArtist(artistLbl.getText().toString());
            }
        });

        add(trackLbl).growX().left().row();
        add(artistLbl).growX().left().row();
    }

    @Override
    public void onProjectSelected(Object source, ProjectSelectedEvent event) {
        if(source == this) return;
        LocalDatabase.DataSet dataSet = event.dataSet;
        if(dataSet == null) return;
        trackLbl.setText(dataSet.getTrackName());
        artistLbl.setText(dataSet.getArtist());
    }
}
