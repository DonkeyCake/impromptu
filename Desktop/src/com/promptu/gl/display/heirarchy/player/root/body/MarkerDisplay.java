package com.promptu.gl.display.heirarchy.player.root.body;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.kotcrab.vis.ui.widget.VisTable;
import com.promptu.database.LocalDatabase;
import com.promptu.database.MarkerPoint;
import com.promptu.event.EventBus;
import com.promptu.event.events.ProjectSelectedEvent;
import com.promptu.gl.display.heirarchy.player.root.body.marker.MarkerElement;

/**
 * Created by Guy on 25/11/2016.
 */
public class MarkerDisplay extends VisTable implements ProjectSelectedEvent.ProjectSelectedListener {

    private LocalDatabase.DataSet dataSet;

    public MarkerDisplay() {
        super(false);
        EventBus.getInstance().register(this);
        setFillParent(true);
        init();
    }

    private void init() {
        populateMarkers();
    }

    public void populateMarkers() {
         addAction(Actions.sequence(
                 Actions.fadeOut(.3f),
                 Actions.run(this::addMarkersToTable),
                 Actions.fadeIn(.3f)
         ));
    }

    private void addMarkersToTable() {
        clearChildren();
        if(this.dataSet != null)
            this.dataSet.getMarkers().forEach(this::addMarkerToTable);
        add().grow().row();
    }

    private void addMarkerToTable(MarkerPoint marker) {
        MarkerElement element = new MarkerElement(marker);
        add(element).growX().padBottom(8).row();
    }


    @Override
    public void onProjectSelected(Object source, ProjectSelectedEvent event) {
        if(source == this) return;
        this.dataSet = event.dataSet;
        populateMarkers();
    }
}
