package com.promptu.api.internal;

import com.promptu.concurrency.AtomicFloat;
import com.promptu.database.Snowflake;

/**
 *
 * @param <T> The track object for the player
 * @param <F> The file handle type
 */
public interface IPlayer<T, F> {

    void setTrack(F handle);
    T getTrack();

    void update();

    void start();
    void pause();
    void stop();
    float getProgressPercent();
    float getProgress();
    float getDuration();

    void onFinish();

    Snowflake getTrackSnowflake();

    // Atomic support

    default boolean supportAtomic() {
        return false;
    }

    default AtomicFloat getAtomicProgressPercent() {
        return null;
    }
    default AtomicFloat getAtomicProgress() {
        return null;
    }
    default AtomicFloat getAtomicDuration() {
        return null;
    }

    /**
     * Clean up any variables, typically used when using heavier player instances (such as OpenAL)
     * <br/>
     * The standard GC can resolve any internal references, however native references must be cleaned up manually
     */
    void dispose();

}
