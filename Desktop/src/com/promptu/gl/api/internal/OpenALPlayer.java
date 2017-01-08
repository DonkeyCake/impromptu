package com.promptu.gl.api.internal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl3.audio.OpenALSound;
import com.badlogic.gdx.files.FileHandle;
import com.promptu.api.internal.IPlayer;
import com.promptu.concurrency.AtomicFloat;
import com.promptu.database.Snowflake;
import com.promptu.event.events.ShareReferenceEvent;
import com.promptu.utils.ChecksumUtils;

/**
 * Created by Guy on 08/01/2017.
 */
public class OpenALPlayer implements IPlayer<Music, FileHandle> {

    AtomicFloat progress;
    AtomicFloat progressPercent;
    AtomicFloat duration;

    private Music track;
    private boolean isPlaying;
    private FileHandle handle;

    public OpenALPlayer() {
        progress = new AtomicFloat();
        progressPercent = new AtomicFloat();
        duration = new AtomicFloat();
    }

    @Override
    public void setTrack(FileHandle trackHandle) {
        if(track != null) {
            track.stop();
            track.dispose();
            track = null;
            duration.set(0);
        }
        this.handle = trackHandle;
        if(trackHandle == null) return;
        Music music = Gdx.audio.newMusic(trackHandle);
        if(music != null) {
            track = music;
            track.setOnCompletionListener(this::onFinishWrapper);
        }
        Sound sound = Gdx.audio.newSound(trackHandle);
        if(sound != null) {
            if(sound instanceof OpenALSound)
                duration.set(((OpenALSound) sound).duration());
        }

        new ShareReferenceEvent("player.activeInstance", this).fireImmediate(this);
        new ShareReferenceEvent("position.activeTrack", progress).fireImmediate(this);
        new ShareReferenceEvent("duration.activeTrack", duration).fireImmediate(this);
    }

    @Override
    public Music getTrack() {
        return track;
    }

    @Override
    public void update() {
        if(track == null) return;
        if(isPlaying)
            progress.set(track.getPosition());
        progressPercent.set(progress.get()/duration.get());
    }

    @Override
    public void start() {
        if(track == null) return;
        isPlaying = true;
        track.play();
    }

    @Override
    public void pause() {
        if(track == null) return;
        isPlaying = false;
        track.pause();
    }

    @Override
    public void stop() {
        if(track == null) return;
        isPlaying = false;
        track.stop();
    }

    @Override
    public float getProgressPercent() {
        return progressPercent.get();
    }

    @Override
    public float getProgress() {
        return progress.get();
    }

    @Override
    public float getDuration() {
        return duration.get();
    }

    @Override
    public void onFinish() {
        isPlaying = false;
    }

    @Override
    public Snowflake getTrackSnowflake() {
        if(this.track == null) return null;
        // TODO extract to SnowflakeFactory?
        Snowflake snowflake = new Snowflake();
        snowflake.content = this.track.toString();
        try {
            snowflake.checksum = ChecksumUtils.getMD5Checksum(this.handle.file().getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            snowflake.checksum = "Error";
        }
        return snowflake;
    }

    private void onFinishWrapper(Music m) {
        onFinish();
    }

    @Override
    public boolean supportAtomic() {
        return true;
    }

    @Override
    public AtomicFloat getAtomicProgressPercent() {
        return progressPercent;
    }

    @Override
    public AtomicFloat getAtomicProgress() {
        return progress;
    }

    @Override
    public AtomicFloat getAtomicDuration() {
        return duration;
    }

    @Override
    public void dispose() {
        if(track != null) {
            track.stop();
            track.dispose();
            track = null;
        }
    }
}
