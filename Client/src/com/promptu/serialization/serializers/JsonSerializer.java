package com.promptu.serialization.serializers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.promptu.database.MarkerBlock;
import com.promptu.database.MarkerPoint;
import com.promptu.serialization.FileSerializer;
import com.promptu.serialization.adapters.json.MarkerAdapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Created by Guy on 18/11/2016.
 */
public class JsonSerializer implements FileSerializer {

    private Gson gson;

    public JsonSerializer() { this(true); }

    public JsonSerializer(boolean pretty) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(MarkerPoint.class, new MarkerAdapter());
        builder.registerTypeAdapter(MarkerBlock.class, new MarkerAdapter());
        if(pretty)
            builder.setPrettyPrinting();
        gson = builder.create();
    }

    @Override
    public void toFile(Path path, Object obj) {
        String json = gson.toJson(obj);
        try {
            Files.deleteIfExists(path);
            Files.write(path, json.getBytes(),
                    StandardOpenOption.CREATE_NEW,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> T fromFile(Path path, Class<T> cls) throws IOException {
        StringBuilder sb = new StringBuilder();
        Files.readAllLines(path).forEach(sb::append);
        String json = sb.toString();
        return gson.fromJson(json, cls);
    }
}
