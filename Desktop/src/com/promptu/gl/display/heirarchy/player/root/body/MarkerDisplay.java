package com.promptu.gl.display.heirarchy.player.root.body;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.kotcrab.vis.ui.widget.VisTable;
import com.promptu.Configuration;
import com.promptu.database.LocalDatabase;
import com.promptu.database.MarkerPoint;
import com.promptu.event.EventBus;
import com.promptu.event.events.MarkerSwitchEvent;
import com.promptu.event.events.ProjectSelectedEvent;
import com.promptu.event.events.TogglePlaybackEvent;
import com.promptu.gl.display.heirarchy.player.root.body.marker.MarkerElement;
import com.promptu.script.functions.ConfigFunctions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guy on 25/11/2016.
 */
public class MarkerDisplay extends VisTable implements ProjectSelectedEvent.ProjectSelectedListener,
        TogglePlaybackEvent.TogglePlaybackListener, MarkerSwitchEvent.MarkerSwitchListener {

    private LocalDatabase.DataSet dataSet;
    private List<MarkerElement> elements;
    int current = -1;

    public MarkerDisplay() {
        super(false);
        EventBus.getInstance().register(this);
        setFillParent(true);
        init();
    }

    private void init() {
        elements = new ArrayList<>();
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
        elements.add(element);
        shiftTo(-1);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(ConfigFunctions.isDebugMode()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.N))
                shiftTo(current + 1);
            else if (Gdx.input.isKeyJustPressed(Input.Keys.M))
                shiftTo(current - 1);
        }
    }

    public void shiftTo(int mutableIndex) {
        final int[] i = {0};
        mutableIndex = MathUtils.clamp(mutableIndex, -1, elements.size());
        if(mutableIndex == current) return;
        final int finalIndex = mutableIndex;
        final int max = elements.size();
        elements.forEach(e -> {
            float single = (e.getHeight() + Configuration.markerPadding);
            float diff = ((finalIndex - i[0]) * single);
            diff -= single;
            if(e.hasParent())
                diff += e.getParent().getHeight();
            Action moveTo;
            moveTo = Actions.moveTo(0, diff, Configuration.animTime);
            if(i[0] == finalIndex) {
                e.addAction(markerAction(Configuration.markerNext, Configuration.markerNextAlpha,
                        Configuration.animTime, moveTo));
            }else if(i[0] == finalIndex +1) {
                e.addAction(markerAction(Configuration.markerPostproximate, Configuration.markerPostproximateAlpha,
                        Configuration.animTime, moveTo));
            }else{
                if(i[0] > finalIndex)
                    e.addAction(markerAction(Configuration.markerRest, Configuration.markerRestAlpha,
                            Configuration.animTime, moveTo));
                else
                    e.addAction(markerAction(Configuration.markerRest, 0,
                            Configuration.animTime, moveTo));
            }
            i[0]++;
        });
        current = mutableIndex;
    }

    private Action markerAction(Color colour, float alpha, float animTime, Action extra) {
        return Actions.parallel(
                Actions.color(colour, animTime),
                Actions.alpha(alpha, animTime),
                extra
        );
    }

    @Override
    public void onProjectSelected(Object source, ProjectSelectedEvent event) {
        if(source == this) return;
        this.dataSet = event.dataSet;
        populateMarkers();
    }

    @Override
    public void onTogglePlayback(Object source, TogglePlaybackEvent event) {
        if(event.getState() == TogglePlaybackEvent.PlaybackState.STOP)
            shiftTo(-1);
    }

    @Override
    public void onMarkerSwitch(Object source, MarkerSwitchEvent event) {
        shiftTo(event.getActiveMarkerIndex());
    }
}
