package com.promptu.gl.events;

import com.promptu.event.AbstractEvent;
import com.promptu.event.Subscribe;

/**
 * Created by Guy on 28/12/2016.
 */
public class OpenCLIEvent extends AbstractEvent {

    public static interface OpenCLIListener {
        @Subscribe
        void onOpenCLI(Object source, OpenCLIEvent event);
    }

}
