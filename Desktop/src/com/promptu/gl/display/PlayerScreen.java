package com.promptu.gl.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.promptu.gl.display.heirarchy.player.PlayerRoot;
import com.promptu.gl.display.heirarchy.player.RootSidebar;
import com.promptu.render.effect.drop.DropShadowRenderer;

import static com.badlogic.gdx.graphics.GL20.GL_ONE;
import static com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA;

/**
 * Created by Guy on 24/11/2016.
 */
public class PlayerScreen extends AbstractScreen {

    private RootSidebar sidebar;
    DropShadowRenderer shadowRenderer;
    Texture texture;

    public PlayerScreen() {
        shadowRenderer = new DropShadowRenderer();
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
    public void render(float delta) {
        super.render(delta);

        Gdx.graphics.setTitle("FPS: "+Gdx.graphics.getFramesPerSecond());

//        if(true) return;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        texture = shadowRenderer.renderStage(stage);
        stage.getBatch().begin();
        stage.getBatch().setColor(Color.WHITE);
        stage.getBatch().draw(texture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void resize(int w, int h) {
        super.resize(w, h);
        sidebar.size.set(192, h-128);
        sidebar.shownPos.set(w-192, 64);
        sidebar.hiddenPos.set(w, 64);
        sidebar.update();

        shadowRenderer.resize(w, h);
    }
}
