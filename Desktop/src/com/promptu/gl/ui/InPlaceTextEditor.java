package com.promptu.gl.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextField;

/**
 * Created by Guy on 27/01/2016.
 */
public class InPlaceTextEditor extends Group {

    VisLabel label;
    VisTextField field;
    boolean focus = false;

    public InPlaceTextEditor() {
        this("");
    }

    public InPlaceTextEditor(String str) {
        label = new VisLabel(str);
        field = new VisTextField(label.getText().toString());

        setTouchable(Touchable.enabled);

        addActor(label);
        addActor(field);
        field.addAction(Actions.alpha(0));

        label.addListener(new InputListener(){
            @Override
            public boolean handle(Event e) {
                System.out.println("Handling event: "+e);
                return super.handle(e);
            }
        });
        field.setTextFieldListener((textField, c) -> label.setText(textField.getText()));
        field.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if(keycode == Input.Keys.ESCAPE)
                    getStage().unfocus(field);
                return super.keyDown(event, keycode);
            }
        });

        addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(x > label.getX() && x < label.getX()+label.getWidth()) {
                    if(y > label.getY() && y < label.getY()+label.getHeight()) {
                        fieldFocusGained();
                    }
                }
            }
        });
    }

    void fieldFocusGained() {
        assertFieldPos();
        field.setText(label.getText().toString());
        field.clearActions();
        field.addAction(Actions.fadeIn(.4f, Interpolation.fade));
    }
    void fieldFocusLost() {
        assertFieldPos();
        label.setText(field.getText());
        field.clearActions();
        field.addAction(Actions.fadeOut(.4f, Interpolation.fade));
    }

    public void setText(String text) {
        label.setText(text);
        field.setText(text);
    }
    public String getText() { return label.getText().toString(); }

    @Override
    public String toString() {
        return getText();
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        assertFieldPos();
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        assertFieldPos();
    }

    private void assertFieldPos() {

        label.setDebug(false);

        label.setX(0);
        label.setY(0);
        label.setWidth(getWidth());
        label.setHeight(getHeight());

        field.setX(label.getX());
        field.setY(label.getY());
        field.setWidth(label.getWidth());
        field.setHeight(label.getHeight());
    }

    @Override
    public void act(float delta) {
        assertFieldPos();
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        if(getStage().getKeyboardFocus() == field) {
            if(!focus) {
                focus = true;
                fieldFocusGained();
            }
        }else{
            if(focus) {
                focus = false;
                fieldFocusLost();
            }
        }

        super.draw(batch, parentAlpha);
    }
}
