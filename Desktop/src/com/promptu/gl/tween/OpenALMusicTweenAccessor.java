package com.promptu.gl.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.backends.lwjgl3.audio.OpenALMusic;

import static com.promptu.utils.BitwiseUtils.AND;

/**
 * Created by Guy on 08/01/2017.
 */
public class OpenALMusicTweenAccessor implements TweenAccessor<OpenALMusic> {

    public static final int POSITION = 1;
    public static final int VOLUME = 2;

    @Override
    public int getValues(OpenALMusic target, int tweenType, float[] returnValues) {
        int index = 0;
        if(AND(tweenType, POSITION)) returnValues[index++] = target.getPosition();
        if(AND(tweenType, POSITION)) returnValues[index++] = target.getVolume();
        return index;
    }

    @Override
    public void setValues(OpenALMusic target, int tweenType, float[] newValues) {
        int index = 0;
        if(AND(tweenType, POSITION)) target.setPosition(newValues[index++]);
        if(AND(tweenType, POSITION)) target.setVolume(newValues[index++]);
    }
}
