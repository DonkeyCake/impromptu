package com.promptu.gl.display.heirarchy.player.root;

import com.kotcrab.vis.ui.widget.VisTable;
import com.promptu.gl.display.heirarchy.player.root.body.PlayerBodySummary;
import com.promptu.gl.display.heirarchy.player.root.body.WaveformCore;

/**
 * Created by Guy on 24/11/2016.
 */
public class PlayerBody extends VisTable {

    public PlayerBody() {
        super(false);
        init();
    }

    private void init() {
        add(new PlayerBodySummary()).growX().height(128).padBottom(2).row();
        add(new WaveformCore()).growX().height(192).padBottom(2).row();
        add("").grow().row();
    }
}
