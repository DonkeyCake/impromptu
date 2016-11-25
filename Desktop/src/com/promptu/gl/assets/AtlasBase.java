package com.promptu.gl.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Guy on 24/11/2016.
 */
public abstract class AtlasBase<T extends Enum> {

    private TextureAtlas atlas;
    private Skin skin;

    protected AtlasBase(String classPath) {
        classPath = AtlasBase.class.getPackage().getName().replace(".", "/") + classPath;
        System.out.println(classPath);
        atlas = new TextureAtlas(Gdx.files.classpath(classPath));
        (skin = new Skin()).addRegions(atlas);
    }

    public abstract String getRef(T ref);

    public TextureRegion getIconRegion(T ref) {
        return skin.getRegion(getRef(ref));
    }

    public TextureRegionDrawable getRegionDrawable(T ref) {
        return new TextureRegionDrawable(getIconRegion(ref));
    }

    public NinePatch getIconPatch(T ref) {
        return skin.getPatch(getRef(ref));
    }

    public NinePatchDrawable getPatchDrawable(T ref) {
        return new NinePatchDrawable(getIconPatch(ref));
    }


}
