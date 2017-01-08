package com.promptu.api.internal;

import com.promptu.database.MarkerBlock;
import com.promptu.database.MarkerPoint;
import com.promptu.event.EventBus;
import com.promptu.event.events.ShareReferenceEvent;
import com.promptu.runnable.TriConsumer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guy on 08/01/2017.
 */
public class MarkerController implements ShareReferenceEvent.ShareReferenceListener {

    protected List<MarkerPoint> currentMarkers;
    protected IPlayer activePlayer;

    protected TriConsumer<List<MarkerPoint>, MarkerPoint, Integer> onSwitch;
    protected TriConsumer<List<MarkerPoint>, MarkerPoint, Integer> onBlockEnd;
    private float previousTick;

    public MarkerController(IPlayer activePlayer) {
        this.activePlayer = activePlayer;
        currentMarkers = new ArrayList<>();
        EventBus.getInstance().register(this);
        previousTick = -1;
    }

    public void update() {
        if(activePlayer == null) return;
        float currentTick = activePlayer.getProgress();
        if(onSwitch != null) {
            for (MarkerPoint marker : currentMarkers) {
                if (marker.startTime().tickIndex() >= previousTick && marker.startTime().tickIndex() <= currentTick)
                    onSwitch.apply(currentMarkers, marker, currentMarkers.indexOf(marker));
                if(onBlockEnd != null) {
                    if(marker instanceof MarkerBlock) {
                        MarkerBlock b = (MarkerBlock) marker;
                        if (b.endTime().tickIndex() >= previousTick && b.endTime().tickIndex() <= currentTick)
                            onBlockEnd.apply(currentMarkers, marker, currentMarkers.indexOf(marker));
                    }
                }
            }
        }
        previousTick = currentTick;
    }

    public List<MarkerPoint> getCurrentMarkers() {
        return currentMarkers;
    }

    public void setCurrentMarkers(List<MarkerPoint> currentMarkers) {
        this.currentMarkers = currentMarkers;
    }

    public IPlayer getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(IPlayer activePlayer) {
        this.activePlayer = activePlayer;
    }

    public void setOnSwitch(TriConsumer<List<MarkerPoint>, MarkerPoint, Integer> onSwitch) {
        this.onSwitch = onSwitch;
    }

    public void setOnBlockEnd(TriConsumer<List<MarkerPoint>, MarkerPoint, Integer> onBlockEnd) {
        this.onBlockEnd = onBlockEnd;
    }

    @Override
    public void onShareReference(Object source, ShareReferenceEvent event) {
        if(source == this) return;
        if(this.activePlayer == null) {
            if (event.getIdentifier().equalsIgnoreCase("player.activeInstance")) {
                if (event.getReference() instanceof IPlayer)
                    this.activePlayer = (IPlayer) event.getReference();
            }
        }
    }

}
