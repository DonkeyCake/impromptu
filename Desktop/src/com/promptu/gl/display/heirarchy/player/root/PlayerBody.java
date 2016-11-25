package com.promptu.gl.display.heirarchy.player.root;

import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisSplitPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.promptu.gl.display.heirarchy.player.root.body.MarkerDisplay;
import com.promptu.gl.display.heirarchy.player.root.body.PlayerBodySummary;
import com.promptu.gl.display.heirarchy.player.root.body.WaveformCore;

/**
 * Created by Guy on 24/11/2016.
 */
public class PlayerBody extends VisTable {

    VisSplitPane splitPane;

    public PlayerBody() {
        super(false);
        init();
    }

    private void init() {

        VisScrollPane markerScroller = new VisScrollPane(new MarkerDisplay());

        splitPane = new VisSplitPane(markerScroller, null, false);
        splitPane.setMinSplitAmount(.1f);
        splitPane.setMaxSplitAmount(.99f);

        add(new PlayerBodySummary()).growX().height(128).padBottom(2).row();
        add(new WaveformCore()).growX().height(192).padBottom(2).row();
        add(splitPane).grow().row();
    }
}
