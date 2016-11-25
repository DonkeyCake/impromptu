package com.promptu.event.events;

import com.promptu.event.AbstractEvent;
import com.promptu.event.Subscribe;

/**
 * Created by Guy on 25/11/2016.
 */
public class ClientInitializationEvent extends AbstractEvent {

    public static interface ClientInitializationListener {
        @Subscribe
        public void onClientInitialization(Object source, ClientInitializationEvent event);
    }

}
