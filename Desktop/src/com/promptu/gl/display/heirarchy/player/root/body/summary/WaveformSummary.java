package com.promptu.gl.display.heirarchy.player.root.body.summary;

import com.kotcrab.vis.ui.widget.VisTable;
import com.promptu.gl.assets.Assets;

/**
 * Created by Guy on 24/11/2016.
 */
public class WaveformSummary extends VisTable {

    public WaveformSummary() {
        super(false);
        init();
    }

    private void init() {
        setBackground(Assets.getInstance().getPatchDrawable(Assets.Refs.WHITEBG));
    }
}
