package com.promptu.gl;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.VisUI;
import com.promptu.event.events.ResizeEvent;
import com.promptu.gl.assets.Assets;
import com.promptu.gl.display.PlayerScreen;
import com.promptu.project.ProjectManager;
import com.promptu.utils.ClasspathUtils;

import static com.promptu.DesktopLauncher.tweenManager;

/**
 * Created by Guy on 24/11/2016.
 */
public class GLLauncher extends Game {

    private static GLLauncher instance;
    public static GLLauncher instance() {
        return instance;
    }

    ResizeEvent resizeEvent = new ResizeEvent();

    @Override
    public void create() {
        instance = this;
        VisUI.load(Gdx.files.classpath(ClasspathUtils.classPathToFilePath(Assets.class)+"/VisUI/uiskin.json"));
        ProjectManager.instance();
        setScreen(new PlayerScreen());
    }

    @Override
    public void render() {
        tweenManager.update(Gdx.graphics.getDeltaTime());
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        resizeEvent.set(width, height).fireImmediate(this);
    }
}