package com.promptu.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.promptu.concurrency.AtomicFloat;

/**
 * Created by Guy on 25/11/2016.
 */
public class AtomicFloatTweenAccessor implements TweenAccessor<AtomicFloat> {

    @Override
    public int getValues(AtomicFloat target, int tweenType, float[] returnValues) {
        returnValues[0] = target.get();
        return 1;
    }

    @Override
    public void setValues(AtomicFloat target, int tweenType, float[] newValues) {
        target.set(newValues[0]);
    }
}
