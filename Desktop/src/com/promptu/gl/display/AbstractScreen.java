package com.promptu.gl.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by Guy on 24/11/2016.
 */
public abstract class AbstractScreen extends InputAdapter implements Screen {

    protected Stage stage;
    protected ScreenViewport viewport;
    protected OrthographicCamera camera;

    protected InputMultiplexer multiplexer;

    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new ScreenViewport(camera);
        stage = new Stage(viewport);


        multiplexer = new InputMultiplexer(this, stage);

        Gdx.input.setInputProcessor(multiplexer);
    }

    protected abstract void update(float delta);

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        update(delta);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int w, int h) {
        camera.setToOrtho(false, w, h);
        viewport.update(w, h, false);
        camera.update(true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
