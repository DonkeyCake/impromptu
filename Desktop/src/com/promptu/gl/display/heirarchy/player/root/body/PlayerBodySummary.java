package com.promptu.gl.display.heirarchy.player.root.body;

import com.kotcrab.vis.ui.widget.VisTable;
import com.promptu.gl.assets.Assets;
import com.promptu.gl.display.heirarchy.player.root.body.summary.SummaryTrackDetails;
import com.promptu.gl.display.heirarchy.player.root.body.summary.WaveformSummary;

/**
 * Created by Guy on 24/11/2016.
 */
public class PlayerBodySummary extends VisTable {

    public PlayerBodySummary() {
        super(false);
        init();
    }

    private void init() {
        setBackground(Assets.getInstance().getPatchDrawable(Assets.Refs.WHITEBG));

        add(new SummaryTrackDetails()).growX();
        add(new WaveformSummary()).growX();
    }
}
