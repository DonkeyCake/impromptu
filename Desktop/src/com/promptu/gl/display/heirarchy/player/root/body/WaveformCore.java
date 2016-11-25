package com.promptu.gl.display.heirarchy.player.root.body;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl3.audio.OpenALAudio;
import com.badlogic.gdx.backends.lwjgl3.audio.OpenALSound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kotcrab.vis.ui.widget.VisTable;
import com.promptu.concurrency.AtomicFloat;
import com.promptu.database.LocalDatabase;
import com.promptu.event.EventBus;
import com.promptu.event.events.*;
import com.promptu.event.events.TogglePlaybackEvent.PlaybackState;
import com.promptu.fx.display.ActivePaneController;
import com.promptu.gl.assets.Assets;
import com.promptu.gl.utils.TextureCache;
import com.promptu.project.ProjectManager;
import com.promptu.utils.ClasspathUtils;

import java.io.File;

import static com.promptu.DesktopLauncher.tweenManager;

/**
 * Created by Guy on 24/11/2016.
 */
public class WaveformCore extends VisTable implements ProjectSelectedEvent.ProjectSelectedListener,
        TogglePlaybackEvent.TogglePlaybackListener, CloseRequestEvent.CloseRequestListener,
        TrackSelectedEvent.TrackSelectedListener {

    private Sprite waveformSprite;
    private Sprite barSprite;
    private Image img;
    private Music track;
    private AtomicFloat position;
    private float duration;
    private float currentX = 0;

    public WaveformCore() {
        super(false);
        EventBus.getInstance().register(this);

        init();
    }

    private void init() {
        position = new AtomicFloat();
        new ShareReferenceEvent("position.activeTrack", position).fire(this);

        barSprite = new Sprite(TextureCache.PIXEL());
        FileHandle classpath = Gdx.files.classpath(ClasspathUtils.classPathToFilePath(ActivePaneController.class) + "/rainingwaveform.png");
        waveformSprite = new Sprite(new Texture(classpath));
        img = new Image(waveformSprite.getTexture());
        setBackground(Assets.getInstance().getPatchDrawable(Assets.Refs.WHITEBG));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if(track != null) {
            if(track.isPlaying())
                position.set(track.getPosition());
        }else position.set(0);

        float t = 0;
        t = position.floatValue() / duration;

        if(track != null)
            currentX = (float) -lerp(-0, getWidth()*5, t);
        else currentX = 0;

        currentX += 96;

        Vector2 v = localToStageCoordinates(new Vector2(currentX, 0));
        Vector2 w = localToStageCoordinates(new Vector2(96, 0));

        waveformSprite.setColor(1, 1, 1, 1);
        waveformSprite.setBounds(v.x, v.y, getWidth()*5, getHeight());
        waveformSprite.draw(batch, parentAlpha);

        barSprite.setColor(1, 0, 0, 0.7f);
        barSprite.setBounds(w.x, w.y, 5, getHeight());
        barSprite.draw(batch, parentAlpha);

    }

    @Override
    public void onProjectSelected(Object source, ProjectSelectedEvent event) {
        if(source == null) return;
        if(event.dataSet == null) return;
        Gdx.app.postRunnable(() -> {
            LocalDatabase.DataSet dataSet = event.dataSet;
            File tmpFile = new File(dataSet.getFingerprintWaveform().replace("file:/", ""));
            FileHandle waveform;
            if(tmpFile.exists()) {
                waveform = Gdx.files.absolute(tmpFile.getAbsolutePath());
            }else
                waveform = Gdx.files.classpath(ClasspathUtils.classPathToFilePath(ActivePaneController.class)+"/rainingwaveform.png");
            Texture tex = new Texture(waveform);
            waveformSprite.setTexture(tex);

            if(track != null) {
                track.stop();
                track.dispose();
                track = null;
            }

            File audioFile = new File(dataSet.getTrackPath().replace("file:/", ""));
            if(!audioFile.exists()) return;
            FileHandle handle = Gdx.files.absolute(audioFile.getAbsolutePath());
            Sound sound = Gdx.audio.newSound(handle);
            if(sound instanceof OpenALSound) {
                duration = ((OpenALSound)sound).duration();
                ProjectManager.instance().dataSet.setDuration(duration);
            }
            sound.dispose();
            track = Gdx.audio.newMusic(handle);
        });
    }

    @Override
    public void onTogglePlayback(Object source, TogglePlaybackEvent event) {
        if(source == this) return;
        if(track == null) return;
        Gdx.app.postRunnable(() -> togglePlayback(event.getState()));
    }

    public void togglePlayback(PlaybackState state) {
        System.out.println(state.name());
        if(track == null) return;
        switch (state) {
            case PLAY:  playTrack();  break;
            case PAUSE: pauseTrack(); break;
            case STOP:  stopTrack();  break;
        }
    }

    private void playTrack() {
        if(track == null) return;
        track.play();
    }
    private void pauseTrack() {
        if(track == null) return;
        track.pause();
    }
    private void stopTrack() {
        if(track == null) return;
        track.stop();
        Tween.to(position, 0, .5f).target(0).ease(TweenEquations.easeInOutSine).start(tweenManager);
    }

    private double lerp(double x, double y, double t) {
        return x + (y - x) * t;
    }

    @Override
    public void onCloseRequest(Object source, CloseRequestEvent event) {
        if(source == this) return;
        if(track != null) {
            track.stop();
            track.dispose();
            track = null;
        }
        if(Gdx.audio instanceof OpenALAudio) {
            OpenALAudio audio = (OpenALAudio) Gdx.audio;
            audio.dispose();
        }
    }

    @Override
    public void onTrackSelected(Object source, TrackSelectedEvent event) {
        if(source == this) return;
        LocalDatabase.DataSet dataSet = ProjectManager.instance().dataSet;
        File audioFile = new File(dataSet.getTrackPath().replace("file:/", ""));
        if(track != null) {
            track.stop();
            track.dispose();
            track = null;
        }
        FileHandle handle = Gdx.files.absolute(audioFile.getAbsolutePath());
        Sound sound = Gdx.audio.newSound(handle);
        if(sound instanceof OpenALSound) {
            duration = (long) (((OpenALSound)sound).duration()*1000L);
            dataSet.setDuration(duration);
        }
        sound.dispose();
        track = Gdx.audio.newMusic(handle);
    }
}
