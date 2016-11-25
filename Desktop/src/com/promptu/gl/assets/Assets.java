package com.promptu.gl.assets;

public class Assets extends AtlasBase<Assets.Refs> {

    private static Assets instance;
    public static Assets getInstance() {
        if (instance == null) instance = new Assets();
        return instance;
    }

    private Assets() {
        super("/assets.atlas");
    }

    @Override
    public String getRef(Refs ref) {
        return ref.ref;
    }

    public enum Refs {
        LOGO("logo"),
        WHITEBG("whiteBG"),
        BLUEBG("blueBG"),
        HAMBURGER("hamburger"),
        PLAY("play"),
        PAUSE("pause"),
        BACK("back"),
        TEXTFIELD("textField"),
        ;

        public final String ref;
        private Refs(String ref) {
            this.ref = ref;
        }
    }

}