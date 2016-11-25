package com.promptu.gl.display.heirarchy.player.root.body.summary;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.promptu.database.LocalDatabase;
import com.promptu.event.EventBus;
import com.promptu.event.events.TrackSelectedEvent;
import com.promptu.gl.GLLauncher;

/**
 * Created by Guy on 24/11/2016.
 */
public class SummaryTrackDetails extends VisTable implements TrackSelectedEvent.TrackSelectedListener {

    VisLabel trackLbl;
    VisLabel artistLbl;

    public SummaryTrackDetails() {
        super(false);
        EventBus.getInstance().register(this);
        init();
    }

    private void init() {
        trackLbl = new VisLabel("");
        artistLbl = new VisLabel("");

        trackLbl.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                GLLauncher.instance().dataSet.setTrackName(trackLbl.getText().toString());
            }
        });
        artistLbl.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                GLLauncher.instance().dataSet.setArtist(artistLbl.getText().toString());
            }
        });

        add(trackLbl).growX().left().row();
        add(artistLbl).growX().left().row();
    }

    @Override
    public void onTrackSelected(Object source, TrackSelectedEvent event) {
        if(source == this) return;
        LocalDatabase.DataSet dataSet = event.dataSet;
        if(dataSet == null) return;
        trackLbl.setText(dataSet.getTrackName());
        artistLbl.setText(dataSet.getArtist());
    }
}
