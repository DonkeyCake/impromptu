package com.promptu.serialization;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by Guy on 18/11/2016.
 */
public interface FileSerializer {

    void toFile(Path path, Object obj);
    <T> T fromFile(Path path, Class<T> cls) throws IOException;

}
