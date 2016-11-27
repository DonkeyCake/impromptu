package com.promptu.gl.display.heirarchy.player.root.body.marker;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import com.promptu.database.MarkerPoint;
import com.promptu.gl.assets.Assets;
import com.promptu.gl.ui.InPlaceTextEditor;

/**
 * Created by Guy on 25/11/2016.
 */
public class MarkerElement extends VisTable {

    private MarkerPoint marker;

    private InPlaceTextEditor headerTxt;
    private InPlaceTextEditor bodyTxt;
    private Button deleteBtn;


    public MarkerElement(MarkerPoint marker) {
        super(false);
        this.marker = marker;
        setBackground(Assets.getInstance().getPatchDrawable(Assets.Refs.BLUEBG));
        init();
    }

    private void init() {
        headerTxt = new InPlaceTextEditor(this.marker.header());
        bodyTxt = new InPlaceTextEditor(this.marker.text());

        float baseSize = 16;
        float headerSize = baseSize*2;
        float bodySize = baseSize*1.5f;
        float btnSize = baseSize*1.5f;

        deleteBtn = new Button(VisUI.getSkin());
        Image deleteImg = new Image(Assets.getInstance().getRegionDrawable(Assets.Refs.DELETE));
        deleteImg.setSize(btnSize, btnSize);
        deleteBtn.addActor(deleteImg);

        add(headerTxt).left().colspan(2).padLeft(4).height(headerSize).padBottom(8).growX().row();
        add().colspan(2).padBottom(8).growX().row();
        add(bodyTxt).left().padLeft(8).height(bodySize).growX();
        add(deleteBtn).right().size(btnSize).padRight(4).padBottom(4).row();
    }

    public void shiftTo(int index) {

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
