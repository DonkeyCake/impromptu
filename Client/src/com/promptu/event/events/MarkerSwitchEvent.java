package com.promptu.event.events;

import com.promptu.database.MarkerPoint;
import com.promptu.event.AbstractEvent;
import com.promptu.event.Subscribe;

import java.util.List;

/**
 * Created by Guy on 26/11/2016.
 */
public class MarkerSwitchEvent extends AbstractEvent {

    public MarkerPoint activeMarker;
    public List<MarkerPoint> markers;

    public MarkerSwitchEvent() {
    }

    public MarkerSwitchEvent(MarkerPoint activeMarker, List<MarkerPoint> markers) {
        this.activeMarker = activeMarker;
        this.markers = markers;
    }

    public MarkerPoint getActiveMarker() {
        return activeMarker;
    }

    public void setActiveMarker(MarkerPoint activeMarker) {
        this.activeMarker = activeMarker;
    }

    public List<MarkerPoint> getMarkers() {
        return markers;
    }

    public void setMarkers(List<MarkerPoint> markers) {
        this.markers = markers;
    }

    public static interface MarkerSwitchListener {
        @Subscribe
        public void onMarkerSwitch(Object source, MarkerSwitchEvent event);
    }

}
