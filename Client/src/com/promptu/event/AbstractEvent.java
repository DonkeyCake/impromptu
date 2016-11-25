package com.promptu.event;

/**
 * Created by Guy on 24/11/2016.
 */
public class AbstractEvent {

    public void fire(final Object source) {
        EventBus.getInstance().post(source, this);
    }

}
