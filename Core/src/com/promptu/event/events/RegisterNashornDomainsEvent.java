package com.promptu.event.events;

import com.promptu.event.AbstractEvent;
import com.promptu.event.Subscribe;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Guy on 28/12/2016.
 */
public class RegisterNashornDomainsEvent extends AbstractEvent {

    private Set<Class<?>> functionDomains;

    public RegisterNashornDomainsEvent() {
        this(new LinkedHashSet<>());
    }

    public RegisterNashornDomainsEvent(Set<Class<?>> functionDomains) {
        this.functionDomains = functionDomains;
    }

    public Set<Class<?>> getFunctionDomains() {
        return functionDomains;
    }

    public void setFunctionDomains(Set<Class<?>> functionDomains) {
        this.functionDomains = functionDomains;
    }

    public static interface RegisterNashornDomainsListener {
        @Subscribe
        void onRegisterNashornDomains(Object source, RegisterNashornDomainsEvent event);
    }

}
