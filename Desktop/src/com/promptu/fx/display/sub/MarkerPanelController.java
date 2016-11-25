package com.promptu.fx.display.sub;

import com.promptu.database.MarkerPoint;
import com.promptu.fx.display.ActivePaneController;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Guy on 18/11/2016.
 */
public class MarkerPanelController implements Initializable {

    private MarkerPoint marker;
    private ActivePaneController controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        header.textProperty().addListener(this::headerChange);
        text.textProperty().addListener(this::textChange);
    }

    public void onSelection(MarkerPoint selected) {
        if(marker.equals(selected)) deleteIcon.setVisible(true);
        else deleteIcon.setVisible(false);
    }

    public void setParentController(ActivePaneController controller) {
        this.controller = controller;
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
    @FXML private ImageView deleteIcon;

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

    @FXML
    public void delete(MouseEvent event) {
        if(controller == null) return;
        controller.deleteMarker(marker);
    }

}
