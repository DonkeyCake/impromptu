package com.promptu.gl.events;

import com.promptu.event.AbstractEvent;
import com.promptu.event.Subscribe;

/**
 * Created by Guy on 24/11/2016.
 */
public class SidebarToggleEvent extends AbstractEvent {

    public SidebarState toOpen;

    public SidebarToggleEvent() {}

    public SidebarToggleEvent(SidebarState toOpen) {
        this.toOpen = toOpen;
    }

    public SidebarState isToOpen() { return toOpen; }
    public void setToOpen(SidebarState toOpen) { this.toOpen = toOpen; }

    public static enum SidebarState {
        OPEN,
        TOGGLE,
        CLOSE,
        ;
    }

    public static interface SidebarToggleListener {
        @Subscribe
        void onSidebarToggle(Object source, SidebarToggleEvent event);
    }

}
