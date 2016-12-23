package com.promptu.render.effect.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.promptu.render.effect.EffectRenderer;
import com.promptu.render.effect.shaders.PingPongBlurShader;
import com.promptu.utils.ClasspathUtils;
import com.promptu.utils.GLUtils;
import com.promptu.utils.ScreenshotUtils;

/**
 * Created by Guy on 26/11/2016.
 */
public class DropShadowRenderer extends EffectRenderer {

    FrameBuffer fbo, blurFbo, applyFbo;
    ShaderProgram blurShader, applyShader;
    Texture mask, expand, output;

    public DropShadowRenderer() {
        super();
        initFbo();
    }

    public void initFbo() {
        if(fbo != null) {
            fbo.dispose();
            fbo = null;
        }
        fbo = new FrameBuffer(Pixmap.Format.RGBA4444, (int)(Gdx.graphics.getWidth()* effectScale), (int)(Gdx.graphics.getHeight()* effectScale), false);
        if(blurFbo != null) {
            blurFbo.dispose();
            blurFbo = null;
        }
        blurFbo = new FrameBuffer(Pixmap.Format.RGBA4444, (int)(Gdx.graphics.getWidth()* effectScale), (int)(Gdx.graphics.getHeight()* effectScale), false);
        if(applyFbo != null) {
            applyFbo.dispose();
            applyFbo = null;
        }
        applyFbo = new FrameBuffer(Pixmap.Format.Alpha, (int)(Gdx.graphics.getWidth()* effectScale), (int)(Gdx.graphics.getHeight()* effectScale), false);
    }

    public Texture renderStage(Stage stage) {
        mask = renderGetActorMask(stage);
        expand = renderExpandMask(stage, mask);
        expand = PingPongBlurShader.pingPong(stage.getBatch(), expand, 12);
//        Texture blurFbo = renderBlurMask(stage, expand);
        output = renderApplyDrop(stage, mask, expand);
//        expand.dispose();
        return output;
    }

    private Texture renderGetActorMask(Stage stage) {
        fbo.begin();
        GLUtils.clear();
        batch.setShader(getPrimaryShaderProgram());
        batch.setProjectionMatrix(stage.getCamera().combined);
        batch.begin();
        renderActor(stage.getRoot());
        batch.end();
        if(Gdx.input.isKeyJustPressed(Input.Keys.F2))
            ScreenshotUtils.saveScreenshot(fbo.getWidth(), fbo.getHeight(), "DROP");
        fbo.end();
        return fbo.getColorBufferTexture();
    }

    private Texture renderExpandMask(Stage stage, Texture mask) {
//        fbo.begin();
//        Pixmap map = ScreenUtils.getFrameBufferPixmap(0, 0, fbo.getWidth(), fbo.getHeight());
//        fbo.end();
//        Pixmap blurred = BlurUtils.blurFbo(map, 0, 0, map.getWidth(), map.getHeight(),
//                0, 0, (int)(map.getWidth()* effectScale), (int)(map.getHeight()* effectScale),
//                4, 3, true);
//        if(Gdx.input.isKeyJustPressed(Input.Keys.F3))
//            PixmapIO.writePNG(Gdx.files.local("ss/tmp.png"), blurred);
//        Texture texture = new Texture(blurred);
//        blurred.dispose();
//        return texture;

        blurFbo.begin();
        GLUtils.clearTransparent();
        getBlurShader().begin();
        getBlurShader().setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        getBlurShader().setUniformi("u_iterations", 8);
        getBlurShader().setUniformf("u_strength",  .75f);
        getBlurShader().setUniformf("u_scalar", 1f);
        batch.setShader(getBlurShader());
        batch.setProjectionMatrix(stage.getCamera().combined);
        batch.begin();
        batch.draw(mask, 0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());
        batch.end();
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2))
            ScreenshotUtils.saveScreenshot(blurFbo.getWidth(), blurFbo.getHeight(), "EXPAND");
        getBlurShader().end();
        blurFbo.end();
        return blurFbo.getColorBufferTexture();
    }

    private Texture renderApplyDrop(Stage stage, Texture mask, Texture blur) {
        applyFbo.begin();
        GLUtils.clearTransparent();
        getApplyShader().begin();
        int maskUnit = 8;
        int blurUnit = 9;
        mask.bind(maskUnit);
        blur.bind(blurUnit);
        getApplyShader().setUniformi("u_mask", maskUnit);
        getApplyShader().setUniformi("u_blur", blurUnit);
        getApplyShader().setUniformf("u_strength", 1.2f);
        batch.setShader(getApplyShader());
        batch.setProjectionMatrix(stage.getCamera().combined);
        batch.begin();
        batch.draw(blur, 0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());
        batch.end();
        if(Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            ScreenshotUtils.saveScreenshot(applyFbo.getWidth(), applyFbo.getHeight(), "APPLY");
        }
        getApplyShader().end();
        applyFbo.end();
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        return applyFbo.getColorBufferTexture();
    }

    private void renderActor(Actor actor) {
        if(actor instanceof Group) {
            ((Group) actor).getChildren().forEach(this::renderActor);
        }
        if(actor instanceof DropShadow) {
            if(((DropShadow) actor).dropShadowEnabled()) {
                Vector2 v = actor.localToStageCoordinates(new Vector2(0, 0));
                getPrimaryShaderProgram().setUniformf("u_bounds", v.x, v.y, actor.getWidth(), actor.getHeight());
                actor.draw(batch, 1);
            }
        }
    }


    public ShaderProgram getApplyShader() {
        if (applyShader == null) {
            String root = ClasspathUtils.classPathToFilePath(this.getClass());
            FileHandle vert = Gdx.files.classpath(root + "/apply.vert");
            FileHandle frag = Gdx.files.classpath(root + "/apply.frag");
            applyShader = GLUtils.compileShaderProgram(vert, frag);
        }
        return applyShader;
    }
    public ShaderProgram getBlurShader() {
        if (blurShader == null) {
            String root = ClasspathUtils.classPathToFilePath(this.getClass());
            FileHandle vert = Gdx.files.classpath(root + "/blur.vert");
            FileHandle frag = Gdx.files.classpath(root + "/blur.frag");
            blurShader = GLUtils.compileShaderProgram(vert, frag);
        }
        return blurShader;
    }

    @Override
    protected void compileShaderProgram() {
        String root = ClasspathUtils.classPathToFilePath(this.getClass());
        FileHandle vert = Gdx.files.classpath(root + "/drop.vert");
        FileHandle frag = Gdx.files.classpath(root + "/drop.frag");
        ShaderProgram program = GLUtils.compileShaderProgram(vert, frag);
        System.out.println(program.getLog());
        if(program.isCompiled())
            this.shaderProgram = program;
    }

    @Override
    public void resize(int w, int h) {
        super.resize(w, h);
        initFbo();
    }

    public interface DropShadow {
        default boolean dropShadowEnabled() {
            return true;
        }
    }

}
