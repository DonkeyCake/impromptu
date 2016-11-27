package com.promptu.gl.events;

import com.promptu.event.AbstractEvent;
import com.promptu.event.Subscribe;

/**
 * Created by Guy on 26/11/2016.
 */
public class EffectScaleChangeEvent extends AbstractEvent {

    public float newScale = 1;

    public EffectScaleChangeEvent() {
    }

    public EffectScaleChangeEvent(float newScale) {
        this.newScale = newScale;
    }

    public float getNewScale() {
        return newScale;
    }

    public void setNewScale(float newScale) {
        this.newScale = newScale;
    }

    public static interface EffectScaleChangeListener {
        @Subscribe
        public void onEffectScaleChange(Object source, EffectScaleChangeEvent event);
    }

}
