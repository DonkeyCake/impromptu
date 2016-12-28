package com.promptu;

import com.badlogic.gdx.graphics.Color;
import com.promptu.event.EventBus;
import com.promptu.event.events.RegisterNashornDomainsEvent;
import com.promptu.script.ScriptFunctionDescription;

/**
 * Created by Guy on 28/12/2016.
 */
public class Configuration implements RegisterNashornDomainsEvent.RegisterNashornDomainsListener {

    public static float animTime = .4f;
    public static int markerPadding = 4;

    public static Color markerNext = Color.RED;
    public static Color markerPostproximate = Color.YELLOW;
    public static Color markerRest = Color.GRAY;

    public static float markerNextAlpha = 1f;
    public static float markerPostproximateAlpha = .9f;
    public static float markerRestAlpha = .5f;

    public Configuration() {
        EventBus.getInstance().register(this);
    }

    @Override
    public void onRegisterNashornDomains(Object source, RegisterNashornDomainsEvent event) {
        event.getFunctionDomains().add(this.getClass());
    }

    @ScriptFunctionDescription("Sets the desktop animation timer")
    public static void setAnimationTime(float seconds) { animTime = seconds; }
    @ScriptFunctionDescription("Gets the desktop animation timer")
    public static float getAnimationTime() { return animTime; }

    @ScriptFunctionDescription("Sets the desktop marker padding")
    public static void setMarkerPadding(int markerPadding) { Configuration.markerPadding = markerPadding; }
    @ScriptFunctionDescription("Gets the desktop marker padding")
    public static int getMarkerPadding() { return markerPadding; }


    public static Color getMarkerNext() {
        return markerNext;
    }

    public static Color getMarkerPostproximate() {
        return markerPostproximate;
    }

    public static Color getMarkerRest() {
        return markerRest;
    }
}
