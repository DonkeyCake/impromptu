package com.promptu.script.functions;

import com.promptu.script.ScriptFunctionDescription;

/**
 * Created by Guy on 28/12/2016.
 */
public class ConfigFunctions {

    public static boolean debugMode = false;

    private ConfigFunctions() {

    }

    @ScriptFunctionDescription("Sets a config variable to the specified value")
    public static void setConfig(String key, Object value) {

    }

    public static boolean isDebugMode() {
        return debugMode;
    }

    public static void setDebugMode(boolean debugMode) {
        ConfigFunctions.debugMode = debugMode;
    }
}
