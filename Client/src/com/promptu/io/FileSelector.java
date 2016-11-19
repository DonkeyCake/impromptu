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
        setDirectory(new File("./"));
//        setFilter(new FileChooser.ExtensionFilter("Promptu files", "promptu", "prmpt"));
    }

    private FileChooser chooser;
    private Stage stage;

    public FileSelector setScene(Stage stage) {
        this.stage = stage;
        return this;
    }

    public FileSelector setFilter(FileChooser.ExtensionFilter filter) {
//        chooser.getExtensionFilters().clear();
        chooser.getExtensionFilters().add(filter);
        return this;
    }

    public FileSelector setDirectory(File file) {
        chooser.setInitialDirectory(file);
        return this;
    }

    public FileSelector setTitle(String title) {
        this.chooser.setTitle(title);
        return this;
    }

    public void singleLoad(Consumer<File> withFile) {
        File file = chooser.showOpenDialog(this.stage);
        if(file != null)
            withFile.accept(file);
    }

    public void multiLoad(Consumer<List<File>> withFiles) {
        List<File> files = chooser.showOpenMultipleDialog(this.stage);
        if(files != null)
            withFiles.accept(files);
    }

    public void save(Consumer<File> toFile) {
        File file = chooser.showSaveDialog(this.stage);
        if(file != null)
            toFile.accept(file);
    }

}
