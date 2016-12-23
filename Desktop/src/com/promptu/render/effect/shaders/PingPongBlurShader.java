package com.promptu.render.effect.shaders;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.promptu.event.EventBus;
import com.promptu.event.events.ResizeEvent;

/**
 * Created by Guy on 23/12/2016.
 */
public class PingPongBlurShader implements ResizeEvent.ResizeListener {

    private BlurPassShader horizontal;
    private BlurPassShader vertical;

    private PingPongBlurShader() {
        EventBus.getInstance().register(this);
    }

    public BlurPassShader getHorizontal() {
        if (horizontal == null)
            horizontal = new BlurPassShader(true);
        return horizontal;
    }

    public BlurPassShader getVertical() {
        if (vertical == null)
            vertical = new BlurPassShader(false);
        return vertical;
    }

    public Texture pingPong_impl(Batch batch, Texture in, int flips) {
        flips = Math.abs(flips);
        Texture lastTexture = in;
        for(int i = 0; i < flips; i++) {
            if(i % 2 == 0) lastTexture = getHorizontal().blurTexture(batch, lastTexture);
            else lastTexture = getVertical().blurTexture(batch, lastTexture);
        }
        return lastTexture;
    }

    @Override
    public void onResize(Object source, ResizeEvent event) {
        if(horizontal != null)
            getHorizontal().resize(event.width, event.height);
        if(vertical != null)
            getVertical().resize(event.width, event.height);
    }

    private static PingPongBlurShader instance;
    public static PingPongBlurShader getInstance() {
        if (instance == null)
            instance = new PingPongBlurShader();
        return instance;
    }

    public static Texture pingPong(Batch batch, Texture in, int flips) {
        return getInstance().pingPong_impl(batch, in, flips);
    }

}
