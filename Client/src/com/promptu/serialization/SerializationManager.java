package com.promptu.serialization;

import com.promptu.serialization.serializers.JsonSerializer;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by Guy on 18/11/2016.
 */
public class SerializationManager {

    private FileSerializer serializer;

    private static SerializationManager instance;
    public static SerializationManager instance() {
        if(instance == null) instance = new SerializationManager();
        return instance;
    }

    private SerializationManager() {
        // Change active serializer here
        serializer = new JsonSerializer(true);
    }

    public void toFile(Path path, Object obj) {
        serializer.toFile(path, obj);
    }

    public <T> T fromFile(Path path, Class<T> cls) throws IOException {
        return serializer.fromFile(path, cls);
    }
}
