package com.promptu.gl.display.heirarchy.player.root;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import com.promptu.gl.assets.Assets;
import com.promptu.gl.assets.Cursors;
import com.promptu.gl.events.SidebarToggleEvent;
import com.promptu.helpers.LambdaClassListener;

/**
 * Created by Guy on 24/11/2016.
 */
public class PlayerBannerTop extends VisTable {

    SidebarToggleEvent sidebarToggleEvent;

    public PlayerBannerTop() {
        super(false);
        sidebarToggleEvent = new SidebarToggleEvent();
        init();
    }

    private void init() {
        Image img = new Image(Assets.getInstance().getRegionDrawable(Assets.Refs.LOGO));
        float scl = img.getHeight() / 64;
        float w = img.getWidth() / scl;
        add(img).left().height(64).width(w).pad(8);
        add().growX().height(64);
        TextField.TextFieldStyle style = VisUI.getSkin().get(TextField.TextFieldStyle.class);
        style.background = Assets.getInstance().getPatchDrawable(Assets.Refs.TEXTFIELD);
        TextField field = new TextField("", style);
        field.setMessageText("Search for a song");
        add(field).right().width(320).height(40);

        Button hamburger = new Button(VisUI.getSkin());
        hamburger.addListener(new LambdaClassListener(this::hamburgerClick));
        Image image = new Image(Assets.getInstance().getRegionDrawable(Assets.Refs.HAMBURGER));
        image.setBounds(0, 0, 64, 64);
        hamburger.addActor(image);
        hamburger.addListener(Cursors.MouseListeners.buttonListener());
//        Image image = new Image(Assets.getInstance().getRegionDrawable(Assets.Refs.HAMBURGER));
//        image.setSize(64, 64);
//        hamburger.addActor(image);

        add(hamburger).right().width(64).height(64).row();

        setBackground(Assets.getInstance().getRegionDrawable(Assets.Refs.BLUEBG));
    }

    public void hamburgerClick(InputEvent event, float x, float y) {
        sidebarToggleEvent.setToOpen(SidebarToggleEvent.SidebarState.TOGGLE);
        sidebarToggleEvent.fire(this);
    }

}
