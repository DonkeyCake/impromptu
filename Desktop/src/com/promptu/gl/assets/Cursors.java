package com.promptu.gl.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Created by Guy on 24/11/2016.
 */
public class Cursors extends AtlasBase<Cursors.Refs> {

    private static Cursors instance;
    public static Cursors getInstance() {
        if (instance == null) instance = new Cursors();
        return instance;
    }

    private Cursors() {
        super("/cursors.atlas");
    }

    @Override
    public String getRef(Refs ref) {
        return ref.ref;
    }

    public static class MouseListeners {

        public static InputListener buttonListener() {
            return new InputListener(){
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                }
            };
        }

    }

    public enum Refs {
        AEROARROW0("aero_arrow/aero_arrow-0"),
        AEROARROW1("aero_arrow/aero_arrow-1"),
        AEROARROW2("aero_arrow/aero_arrow-2"),
        AEROARROW3("aero_arrow/aero_arrow-3"),
        AEROARROW4("aero_arrow/aero_arrow-4"),
        AEROARROWL0("aero_arrow_l/aero_arrow_l-0"),
        AEROARROWL1("aero_arrow_l/aero_arrow_l-1"),
        AEROARROWL2("aero_arrow_l/aero_arrow_l-2"),
        AEROARROWL3("aero_arrow_l/aero_arrow_l-3"),
        AEROARROWL4("aero_arrow_l/aero_arrow_l-4"),
        AEROARROWXL0("aero_arrow_xl/aero_arrow_xl-0"),
        AEROARROWXL1("aero_arrow_xl/aero_arrow_xl-1"),
        AEROARROWXL2("aero_arrow_xl/aero_arrow_xl-2"),
        AEROARROWXL3("aero_arrow_xl/aero_arrow_xl-3"),
        AEROARROWXL4("aero_arrow_xl/aero_arrow_xl-4"),
        AEROARROWXL5("aero_arrow_xl/aero_arrow_xl-5"),

        AEROEW0("aero_ew/aero_ew-0"),
        AEROEW1("aero_ew/aero_ew-1"),
        AEROEW2("aero_ew/aero_ew-2"),
        AEROEW3("aero_ew/aero_ew-3"),
        AEROEW4("aero_ew/aero_ew-4"),
        AEROEWL0("aero_ew_l/aero_ew_l-0"),
        AEROEWL1("aero_ew_l/aero_ew_l-1"),
        AEROEWL2("aero_ew_l/aero_ew_l-2"),
        AEROEWL3("aero_ew_l/aero_ew_l-3"),
        AEROEWL4("aero_ew_l/aero_ew_l-4"),
        AEROEWXL0("aero_ew_xl/aero_ew_xl-0"),
        AEROEWXL1("aero_ew_xl/aero_ew_xl-1"),
        AEROEWXL2("aero_ew_xl/aero_ew_xl-2"),
        AEROEWXL3("aero_ew_xl/aero_ew_xl-3"),
        AEROEWXL4("aero_ew_xl/aero_ew_xl-4"),

        AEROLINK0("aero_link/aero_link-0"),
        AEROLINK1("aero_link/aero_link-1"),
        AEROLINK2("aero_link/aero_link-2"),
        AEROLINK3("aero_link/aero_link-3"),
        AEROLINK4("aero_link/aero_link-4"),
        AEROLINKL0("aero_link_l/aero_link_l-0"),
        AEROLINKL1("aero_link_l/aero_link_l-1"),
        AEROLINKL2("aero_link_l/aero_link_l-2"),
        AEROLINKL3("aero_link_l/aero_link_l-3"),
        AEROLINKL4("aero_link_l/aero_link_l-4"),
        AEROLINKXL0("aero_link_xl/aero_link_xl-0"),
        AEROLINKXL1("aero_link_xl/aero_link_xl-1"),
        AEROLINKXL2("aero_link_xl/aero_link_xl-2"),
        AEROLINKXL3("aero_link_xl/aero_link_xl-3"),
        AEROLINKXL4("aero_link_xl/aero_link_xl-4"),

        ;
        public final String ref;
        private Refs(String ref) {
            this.ref = ref;
        }
    }

}
