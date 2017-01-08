package com.promptu.gl.display.heirarchy.player.root.body.marker;

import com.promptu.database.MarkerBlock;
import com.promptu.database.MarkerPoint;
import com.promptu.gl.display.heirarchy.player.root.body.summary.MarkerPosition;

/**
 * Created by Guy on 08/01/2017.
 */
public class MarkerWaveformBlockRenderer {

    public void render(MarkerPoint point, MarkerPosition position) {
        if(point instanceof MarkerBlock)
            renderBlock((MarkerBlock) point, position);
        else renderPoint(point, position);
    }

    // TODO render markers

    protected void renderPoint(MarkerPoint point, MarkerPosition position) {

    }

    protected void renderBlock(MarkerBlock block, MarkerPosition position) {

    }

}
