package com.promptu.render.effect;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.promptu.event.EventBus;
import com.promptu.gl.events.EffectScaleChangeEvent;

/**
 * Created by Guy on 26/11/2016.
 */
public abstract class EffectRenderer implements EffectScaleChangeEvent.EffectScaleChangeListener {

    protected SpriteBatch batch;
    protected ShaderProgram shaderProgram;
    public float effectScale = 1f;

    protected EffectRenderer() {
        batch = new SpriteBatch();
        ShaderProgram.pedantic = false;
        compileShaderProgram();
        EventBus.getInstance().register(this);
    }

    protected ShaderProgram getPrimaryShaderProgram() {
        if (shaderProgram == null) compileShaderProgram();
        return shaderProgram;
    }
    protected abstract void compileShaderProgram();

    public void resize(int w, int h) {}

    @Override
    public void onEffectScaleChange(Object source, EffectScaleChangeEvent event) {
        if(source == this) return;
        this.effectScale = event.newScale;
    }
}
