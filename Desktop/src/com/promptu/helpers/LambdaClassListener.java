package com.promptu.helpers;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by Guy on 24/11/2016.
 */
public class LambdaClassListener extends ClickListener {

    private FunctionalClick action;

    public LambdaClassListener(FunctionalClick action) {
        this.action = action;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        this.action.clicked(event, x, y);
    }

    @FunctionalInterface
    public static interface FunctionalClick {
        void clicked(InputEvent event, float x, float y);
    }

}
