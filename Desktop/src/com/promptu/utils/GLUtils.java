package com.promptu.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Created by Guy on 26/11/2016.
 */
public class GLUtils {

    private static Color transparent = new Color(1, 0, 0, 0);

    public static void clear(Color col) {
        Gdx.gl.glClearColor(col.r, col.g, col.b, col.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }

    public static void clear() { clear(Color.BLACK); }
    public static void clearWhite() { clear(Color.WHITE); }
    public static void clearTransparent() { clear(transparent); }

    public static ShaderProgram compileShaderProgram(FileHandle vert, FileHandle frag) {
        return compileShaderProgram(vert.readString(), frag.readString());
    }
    public static ShaderProgram compileShaderProgram(String vert, String frag) {
        ShaderProgram program = new ShaderProgram(vert, frag);
        System.out.println(program.getLog());
        if(program.isCompiled())
            return program;
        return null;
    }

}
