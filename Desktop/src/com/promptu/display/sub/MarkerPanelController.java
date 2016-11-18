package com.promptu.display.sub;

import com.promptu.database.MarkerPoint;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

/**
 * Created by Guy on 18/11/2016.
 */
public class MarkerPanelController implements Initializable {

    private MarkerPoint marker;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        header.textProperty().addListener(this::headerChange);
        text.textProperty().addListener(this::textChange);
    }

    public void setMarker(MarkerPoint marker) {
        this.marker = marker;
        this.header.setText(this.marker.header());
        this.text.setText(this.marker.text());
    }

    public void headerChange(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if(this.marker == null) return;
        this.marker.header(newValue);
    }
    public void textChange(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if(this.marker == null) return;
        this.marker.text(newValue);
    }

    @FXML private TextField header;
    @FXML private TextField text;
    @FXML private GridPane rootGrid;

    public void setHeader(String header) {
        this.header.setText(header);
    }
    public void setText(String text) {
        this.text.setText(text);
    }

    public void setClass(String cls) {
        rootGrid.getStyleClass().clear();
        rootGrid.getStyleClass().add(cls);
    }

}
