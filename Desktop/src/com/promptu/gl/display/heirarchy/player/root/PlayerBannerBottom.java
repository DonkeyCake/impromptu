package com.promptu.gl.display.heirarchy.player.root;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import com.promptu.event.EventBus;
import com.promptu.event.events.TogglePlaybackEvent;
import com.promptu.gl.assets.Assets;
import com.promptu.gl.assets.Cursors;
import com.promptu.helpers.LambdaClassListener;

import static com.promptu.event.events.TogglePlaybackEvent.PlaybackState.*;

/**
 * Created by Guy on 24/11/2016.
 */
public class PlayerBannerBottom extends VisTable implements TogglePlaybackEvent.TogglePlaybackListener {

    private boolean isPlaying;
    private TogglePlaybackEvent togglePlaybackEvent;
    private Image playPauseImg;

    public PlayerBannerBottom() {
        super(false);
        EventBus.getInstance().register(this);
        isPlaying = false;
        togglePlaybackEvent = new TogglePlaybackEvent();
        init();
    }

    private void init() {
        Button playPause = new Button(VisUI.getSkin());
        Button back = new Button(VisUI.getSkin());

        playPauseImg = new Image(Assets.getInstance().getRegionDrawable(Assets.Refs.PLAY));
        playPauseImg.setBounds(0, 0, 64, 64);
        playPause.addActor(playPauseImg);
        Image image = new Image(Assets.getInstance().getRegionDrawable(Assets.Refs.BACK));
        image.setBounds(0, 0, 64, 64);
        back.addActor(image);

        playPause.addListener(Cursors.MouseListeners.buttonListener());
        back.addListener(Cursors.MouseListeners.buttonListener());

        playPause.addListener(new LambdaClassListener(this::togglePlay));
        back.addListener(new LambdaClassListener(this::back));

        add().left().growX();
        add(back).size(64);
        add().size(32);
        add(playPause).size(64);
        add().right().growX();

        setBackground(Assets.getInstance().getRegionDrawable(Assets.Refs.BLUEBG));
    }

    public void back(InputEvent event, float x, float y) {
        back();
    }
    public void back() {
        isPlaying = false;
        togglePlaybackEvent.setState(STOP).fire(this);
        playPauseImg.setDrawable(Assets.getInstance().getRegionDrawable(Assets.Refs.PLAY));
    }

    public void togglePlay(InputEvent event, float x, float y) {
        if(!isPlaying) play();
        else pause();
    }

    public void play() {
        isPlaying = true;
        togglePlaybackEvent.setState(PLAY).fire(this);
        playPauseImg.setDrawable(Assets.getInstance().getRegionDrawable(Assets.Refs.PAUSE));
    }

    public void pause() {
        isPlaying = false;
        togglePlaybackEvent.setState(PAUSE).fire(this);
        playPauseImg.setDrawable(Assets.getInstance().getRegionDrawable(Assets.Refs.PLAY));
    }

    @Override
    public void onTogglePlayback(Object source, TogglePlaybackEvent event) {
        if(source == this) return;
        Gdx.app.postRunnable(() -> {
            switch (event.getState()) {
                case PLAY:  play(); break;
                case PAUSE: pause(); break;
                case STOP:  back(); break;
            }
        });
    }



}
