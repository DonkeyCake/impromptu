package com.promptu.event.events;

import com.promptu.event.AbstractEvent;
import com.promptu.event.Subscribe;

/**
 * Created by Guy on 23/12/2016.
 */
public class ResizeEvent extends AbstractEvent {

    public float width;
    public float height;

    public ResizeEvent() { this(0, 0); }

    public ResizeEvent(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public ResizeEvent set(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public static interface ResizeListener {
        @Subscribe
        void onResize(Object source, ResizeEvent event);
    }

}
