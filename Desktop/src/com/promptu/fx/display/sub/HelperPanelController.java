package com.promptu.fx.display.sub;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by Guy on 18/11/2016.
 */
public class HelperPanelController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML private TextArea text;

    public void setText(String text) {
        this.text.setText(text);
    }

}
