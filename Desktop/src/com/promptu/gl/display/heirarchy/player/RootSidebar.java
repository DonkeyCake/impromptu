package com.promptu.gl.display.heirarchy.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.kotcrab.vis.ui.widget.VisTable;
import com.promptu.Configuration;
import com.promptu.event.EventBus;
import com.promptu.gl.assets.Assets;
import com.promptu.gl.display.heirarchy.player.sidebar.SidebarBody;
import com.promptu.gl.events.SidebarToggleEvent;
import com.promptu.render.effect.drop.DropShadowRenderer;

/**
 * Created by Guy on 24/11/2016.
 */
public class RootSidebar extends VisTable implements SidebarToggleEvent.SidebarToggleListener, DropShadowRenderer.DropShadow {

    public boolean isShown = false;
    public Vector2 size;
    public Vector2 shownPos;
    public Vector2 hiddenPos;

    public RootSidebar() {
        super(false);
        EventBus.getInstance().register(this);
        size = new Vector2();
        shownPos = new Vector2();
        hiddenPos = new Vector2();
        init();
    }

    private void init() {
        add(new SidebarBody()).growX();
        setBackground(Assets.getInstance().getPatchDrawable(Assets.Refs.BLUEBG));
    }

    public void toggle() {
        if(isShown) hide();
        else show();
    }

    public void show() {
        isShown = true;
        clearActions();
        addAction(Actions.parallel(
                Actions.alpha(0.9f, Configuration.animTime),
                Actions.moveTo(shownPos.x, shownPos.y, Configuration.animTime),
                Actions.sizeTo(size.x, size.y)
        ));
        toFront();
    }

    public void hide() {
        isShown = false;
        clearActions();
        addAction(Actions.parallel(
                Actions.fadeOut(Configuration.animTime),
                Actions.moveTo(hiddenPos.x, hiddenPos.y, Configuration.animTime),
                Actions.sizeTo(size.x, size.y)
        ));
    }

    public void update() {
        Vector2 vec;
        if(isShown) vec = shownPos;
        else vec = hiddenPos;

        addAction(Actions.parallel(
                Actions.moveTo(vec.x, vec.y),
                Actions.sizeTo(size.x, size.y)
        ));
    }

    @Override
    public void onSidebarToggle(Object source, SidebarToggleEvent event) {
        if(source == this) return;
        switch(event.isToOpen()) {
            case OPEN:   show();    break;
            case TOGGLE: toggle();  break;
            case CLOSE:  hide();    break;
        }
    }
}
