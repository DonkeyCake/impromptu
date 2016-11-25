package com.promptu.gl.display.heirarchy.player.root.body.summary;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.kotcrab.vis.ui.widget.VisTable;
import com.promptu.concurrency.AtomicFloat;
import com.promptu.event.EventBus;
import com.promptu.event.events.ProjectSelectedEvent;
import com.promptu.event.events.ShareReferenceEvent;
import com.promptu.fx.display.ActivePaneController;
import com.promptu.gl.assets.Assets;
import com.promptu.gl.utils.TextureCache;
import com.promptu.project.ProjectManager;
import com.promptu.utils.ClasspathUtils;

import java.io.File;

/**
 * Created by Guy on 24/11/2016.
 */
public class WaveformSummary extends VisTable implements ProjectSelectedEvent.ProjectSelectedListener, ShareReferenceEvent.ShareReferenceListener {

    Sprite sprite;
    Sprite line;
    AtomicFloat position;
    Vector2 v;

    public WaveformSummary() {
        super(false);
        sprite = new Sprite();
        line = new Sprite(TextureCache.PIXEL());
        EventBus.getInstance().register(this);
        setTouchable(Touchable.enabled);
        init();
    }

    private void init() {
        v = new Vector2();
        setBackground(Assets.getInstance().getPatchDrawable(Assets.Refs.WHITEBG));

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setCurrentX(getPercFromX(x));
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                setCurrentX(getPercFromX(x));
                super.touchDragged(event, x, y, pointer);
            }
        });
    }

    public float getPercFromX(float x) {
        return MathUtils.clamp(x / getWidth(), 0, 1);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        localToStageCoordinates(v.set(0, 0));
        if(sprite.getTexture() != null) {
            sprite.setBounds(v.x, v.y, getWidth(), getHeight());
            sprite.draw(batch, parentAlpha);
        }
        if(position != null) {
            float x = getCurrentX();
            localToStageCoordinates(v.set(x, 0));
            line.setColor(Color.RED);
            line.setBounds(v.x, v.y, 1, getHeight());
            line.draw(batch, parentAlpha);
        }
    }

    public float getCurrentX() {
        float p = position.floatValue();
        float d = ProjectManager.instance().dataSet.getDuration();
        float perc = p / d;
        return getWidth()*perc;
    }

    public void setCurrentX(float perc) {
        position.set(ProjectManager.instance().dataSet.getDuration() * perc);
    }

    @Override
    public void onProjectSelected(Object source, ProjectSelectedEvent event) {
        if(source == this) return;
        if(event.dataSet == null) return;
        Gdx.app.postRunnable(() -> {
            File tmpFile = new File(event.dataSet.getFingerprintWaveform().replace("file:/", ""));
            FileHandle waveform;
            if(tmpFile.exists()) {
                waveform = Gdx.files.absolute(tmpFile.getAbsolutePath());
            }else
                waveform = Gdx.files.classpath(ClasspathUtils.classPathToFilePath(ActivePaneController.class)+"/rainingwaveform.png");
            Texture tex = new Texture(waveform);
            sprite.setTexture(tex);
            sprite.setU2(1);
            sprite.setV2(1);
        });
    }

    @Override
    public void onShareReference(Object source, ShareReferenceEvent event) {
        if(source == this) return;
        Object o = event.getReference();
        if(o == null) return;
        if(o instanceof AtomicFloat) {
            if (event.getIdentifier().equalsIgnoreCase("position.activeTrack"))
                position = (AtomicFloat) o;
        }
    }
}
