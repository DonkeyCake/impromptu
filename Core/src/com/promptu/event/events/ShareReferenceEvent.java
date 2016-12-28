package com.promptu.event.events;

import com.promptu.event.AbstractEvent;
import com.promptu.event.Subscribe;

/**
 * Created by Guy on 25/11/2016.
 */
public class ShareReferenceEvent extends AbstractEvent {

    public String identifier = "";
    public Object reference;

    public ShareReferenceEvent(String identifier) {
        this(identifier, null);
    }

    public ShareReferenceEvent(Object reference) {
        this(null, reference);
    }

    public ShareReferenceEvent(String identifier, Object reference) {
        this.identifier = identifier;
        this.reference = reference;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Object getReference() {
        return reference;
    }

    public void setReference(Object reference) {
        this.reference = reference;
    }

    public static interface ShareReferenceListener {
        @Subscribe
        public void onShareReference(Object source, ShareReferenceEvent event);
    }

}
