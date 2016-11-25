package com.promptu.gl.display;

import com.badlogic.gdx.Gdx;
import com.promptu.gl.display.heirarchy.player.PlayerRoot;
import com.promptu.gl.display.heirarchy.player.RootSidebar;

/**
 * Created by Guy on 24/11/2016.
 */
public class PlayerScreen extends AbstractScreen {

    private RootSidebar sidebar;

    public PlayerScreen() {
    }

    @Override
    public void show() {
        super.show();
        stage.addActor(new PlayerRoot());
        stage.addActor(sidebar = new RootSidebar());

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    protected void update(float delta) {
        stage.setDebugAll(false);
    }

    @Override
    public void resize(int w, int h) {
        super.resize(w, h);
        sidebar.size.set(192, h-128);
        sidebar.shownPos.set(w-192, 64);
        sidebar.hiddenPos.set(w, 64);
        sidebar.update();
    }
}
