package com.promptu.event.events;

import com.promptu.event.AbstractEvent;
import com.promptu.event.Subscribe;

/**
 * Created by Guy on 25/11/2016.
 */
public class CloseRequestEvent extends AbstractEvent {

    public static interface CloseRequestListener {
        @Subscribe
        void onCloseRequest(Object source, CloseRequestEvent event);
    }

}
