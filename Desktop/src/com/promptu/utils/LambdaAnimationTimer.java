package com.promptu.utils;

import javafx.animation.AnimationTimer;

import java.util.function.Consumer;

/**
 * Created by Guy on 18/11/2016.
 */
public class LambdaAnimationTimer extends AnimationTimer {

    private Consumer<Long> handle;

    public LambdaAnimationTimer(Consumer<Long> handle) {
        this.handle = handle;
    }

    @Override
    public void handle(long now) {
        this.handle.accept(now);
    }
}
