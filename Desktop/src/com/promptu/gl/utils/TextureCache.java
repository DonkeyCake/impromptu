package com.promptu.gl.utils;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Guy on 25/11/2016.
 */
public class TextureCache {

    private static Texture PIXEL;
    public static Texture PIXEL() {
        if(PIXEL == null) {
            Pixmap map = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            map.setColor(1, 1, 1, 1);
            map.drawPixel(0, 0);
            PIXEL = new Texture(map);
            map.dispose();
        }
        return PIXEL;
    }

}
