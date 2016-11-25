package com.promptu.gl.display.skin;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.VisUI;

/**
 * Created by Guy on 24/11/2016.
 */
public class SkinManager {

    private static SkinManager instance;
    public static SkinManager instance() {
        if(instance == null) instance = new SkinManager();
        return instance;
    }

    public final Skin visX1;
    public final Skin visX2;

    private SkinManager() {
        visX1 = new Skin(VisUI.SkinScale.X1.getSkinFile());
        visX2 = new Skin(VisUI.SkinScale.X2.getSkinFile());
    }
}
