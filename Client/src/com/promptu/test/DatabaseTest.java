package com.promptu.test;

import com.promptu.database.LocalDatabase;
import com.promptu.database.MarkerBlock;
import com.promptu.database.MarkerPoint;

/**
 * Created by Guy on 18/11/2016.
 */
public class DatabaseTest {

    public static void main(String[] args) {
        LocalDatabase db = LocalDatabase.instance();
        MarkerPoint markerPoint1 = new MarkerPoint();
        MarkerPoint markerPoint2 = new MarkerPoint();
        MarkerPoint markerPoint3 = new MarkerPoint();

        MarkerPoint markerBlock1 = new MarkerBlock();
        MarkerPoint markerBlock2 = new MarkerBlock();
        MarkerPoint markerBlock3 = new MarkerBlock();

        markerPoint1.mbid("test1");
        markerPoint1.colourHex("#FF0000");
        markerPoint2.mbid("test2");
        markerPoint2.colourHex("#00FF00");
        markerPoint3.mbid("test3");
        markerPoint3.colourHex("#0000FF");

        markerBlock1.mbid("test1");
        markerBlock1.colourHex("#FF0000");
        markerBlock2.mbid("test2");
        markerBlock2.colourHex("#00FF00");
        markerBlock3.mbid("test3");
        markerBlock3.colourHex("#0000FF");

        db.addMarker(markerPoint1);
        db.addMarker(markerPoint2);
        db.addMarker(markerPoint3);

        db.addMarker(markerBlock1);
        db.addMarker(markerBlock2);
        db.addMarker(markerBlock3);

        db.addWaveform("test1", "Waveform1");
        db.addWaveform("test2", "Waveform2");
        db.addWaveform("test3", "Waveform3");

        db.outputToFile("localDB.json");
    }

}
