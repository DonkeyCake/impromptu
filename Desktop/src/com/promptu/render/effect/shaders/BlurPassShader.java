package com.promptu.render.effect.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.promptu.utils.ClasspathUtils;
import com.promptu.utils.GLUtils;
import com.promptu.utils.ScreenshotUtils;

/**
 * Created by Guy on 23/12/2016.
 */
public class BlurPassShader {

    private ShaderProgram shaderProgram;
    private FrameBuffer fbo;
    public final boolean horizontal;
    private float scale;

    public BlurPassShader(boolean horizontal) {
        this(horizontal, 1);
    }

    public BlurPassShader(boolean horizontal, float scale) {
        this.horizontal = horizontal;
        this.scale = scale;
        compile();
    }

    private void compile() {
        String root = ClasspathUtils.classPathToFilePath(this.getClass());
        String vert = Gdx.files.classpath(root + "/blur.vert").readString();
        String frag = Gdx.files.classpath(root + "/blur.frag").readString();

        vert = vert.replaceAll("\\{\\{HORIZONTAL}}", horizontal ? "#define HORIZONTAL" : "");
        frag = frag.replaceAll("\\{\\{HORIZONTAL}}", horizontal ? "#define HORIZONTAL" : "");

        shaderProgram = GLUtils.compileShaderProgram(vert, frag);
    }

    private FrameBuffer fbo() {
        if(fbo == null)
            fbo = new FrameBuffer(Pixmap.Format.RGBA8888, (int)(Gdx.graphics.getWidth() * scale), (int)(Gdx.graphics.getHeight() * scale), true);
        return fbo;
    }

    public void resize(float width, float height) {
        if(fbo != null) {
            fbo.dispose();
            fbo = null;
        }
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, (int)(width * scale), (int)(height * scale), true);
    }

    public Texture blurTexture(Batch batch, Texture in) {
        ShaderProgram shader = batch.getShader();
        fbo().begin();
        GLUtils.clear();
        batch.setShader(shaderProgram);
        boolean closeOnFinish = false;
        if(!batch.isDrawing()) {
            closeOnFinish = true;
            batch.begin();
        }
        batch.draw(in, 0, 0, fbo().getWidth(), fbo().getHeight());
        if(closeOnFinish)
            batch.end();
        batch.setShader(shader);
        if(Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            ScreenshotUtils.saveScreenshot(fbo().getWidth(), fbo().getHeight(), "blur/pass_"+horizontal);
        }
        fbo().end();
        return fbo.getColorBufferTexture();
    }

}
