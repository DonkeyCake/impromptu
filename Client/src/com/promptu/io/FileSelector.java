package com.promptu.io;

import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Guy on 19/11/2016.
 */
public class FileSelector {

    private static FileSelector instance;
    public static FileSelector instance() {
        if(instance == null) instance = new FileSelector();
        return instance;
    }

    private FileSelector() {
        chooser = new FileChooser();
    }

    private FileChooser chooser;
    private Stage stage;

    public FileSelector setScene(Stage stage) {
        this.stage = stage;
        return this;
    }

    public FileSelector setTitle(String title) {
        this.chooser.setTitle(title);
        return this;
    }

    public void singleLoad(Consumer<File> withFile) {
        File file = chooser.showOpenDialog(this.stage);
        withFile.accept(file);
    }

    public void multiLoad(Consumer<List<File>> withFiles) {
        List<File> files = chooser.showOpenMultipleDialog(this.stage);
        withFiles.accept(files);
    }

    public void save(Consumer<File> toFile) {
        File file = chooser.showSaveDialog(this.stage);
        toFile.accept(file);
    }

}
