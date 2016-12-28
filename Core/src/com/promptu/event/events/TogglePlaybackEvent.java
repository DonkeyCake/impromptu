package com.promptu.event.events;

import com.promptu.event.AbstractEvent;
import com.promptu.event.Subscribe;

/**
 * Created by Guy on 24/11/2016.
 */
public class TogglePlaybackEvent extends AbstractEvent {

    public PlaybackState state;

    public TogglePlaybackEvent() {}
    public TogglePlaybackEvent(PlaybackState state) {
        this.state = state;
    }

    public PlaybackState getState() { return state; }
    public TogglePlaybackEvent setState(PlaybackState state) {
        this.state = state;
        return this;
    }

    public static enum PlaybackState {
        PLAY,
        PAUSE,
        STOP
    }

    public static interface TogglePlaybackListener {
        @Subscribe
        void onTogglePlayback(Object source, TogglePlaybackEvent event);
    }

}
