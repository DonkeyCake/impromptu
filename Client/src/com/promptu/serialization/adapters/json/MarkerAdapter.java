package com.promptu.serialization.adapters.json;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.promptu.database.MarkerBlock;
import com.promptu.database.MarkerPoint;
import com.promptu.database.MarkerType;

import java.io.IOException;

/**
 * Created by Guy on 18/11/2016.
 */
public class MarkerAdapter extends TypeAdapter<MarkerPoint> {

    @Override
    public void write(JsonWriter jsonWriter, MarkerPoint markerPoint) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("type").value(markerPoint.getType().name());
        jsonWriter.name("mbid").value(markerPoint.mbid());
        jsonWriter.name("startTime").value(markerPoint.startTime().toString());
        if(markerPoint instanceof MarkerBlock)
            jsonWriter.name("endTime").value(((MarkerBlock)markerPoint).endTime().toString());
        jsonWriter.name("colourHex").value(markerPoint.colourHex());
        jsonWriter.endObject();
    }

    @Override
    public MarkerPoint read(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        MarkerType type = MarkerType.POINT;
        String mbid = "";
        String startTimeStr = "";
        String endTimeStr = "";
        String colourHex = "";
        while(jsonReader.hasNext()) {
            switch(jsonReader.nextName()) {
                case "type": type = MarkerType.valueOf(jsonReader.nextString()); break;
                case "mbid": mbid = jsonReader.nextString(); break;
                case "startTime": startTimeStr = jsonReader.nextString(); break;
                case "endTime": endTimeStr = jsonReader.nextString(); break;
                case "colourHex": colourHex = jsonReader.nextString(); break;
            }
        }
        jsonReader.endObject();

        MarkerPoint point;
        if(type == MarkerType.BLOCK) {
            point = new MarkerBlock();
            ((MarkerBlock)point).endTime(endTimeStr);
        }else{
            point = new MarkerPoint();
        }
        point.mbid(mbid);
        point.startTime(startTimeStr);
        point.colourHex(colourHex);

        return point;
    }
}
