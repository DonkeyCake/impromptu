package com.promptu.gl.display.heirarchy.player;

import com.kotcrab.vis.ui.widget.VisTable;
import com.promptu.gl.display.heirarchy.player.root.PlayerBannerBottom;
import com.promptu.gl.display.heirarchy.player.root.PlayerBannerTop;
import com.promptu.gl.display.heirarchy.player.root.PlayerBody;

/**
 * Created by Guy on 24/11/2016.
 */
public class PlayerRoot extends VisTable {

    public PlayerRoot() {
        super(false);
        setFillParent(true);
        init();
    }

    private void init() {
        add(new PlayerBannerTop()).growX().height(64).row();
        add(new PlayerBody()).grow().row();
        add(new PlayerBannerBottom()).growX().height(64).row();
    }

}
