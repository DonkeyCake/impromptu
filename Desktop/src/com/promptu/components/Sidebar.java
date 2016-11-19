package com.promptu.components;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Created by Guy on 19/11/2016.
 */
public class Sidebar extends VBox {

    private Node controlButton;

    public Node getControlButton() {
        return controlButton;
    }

    public Sidebar(double width, ImageView btn, Node... children) {
        getStyleClass().add("sidebar");
        this.setPrefWidth(width);
        this.setMinWidth(0);
        this.setVisible(false);
        setAlignment(Pos.CENTER);
        getChildren().addAll(children);
        controlButton = btn;
        controlButton.getStyleClass().add("hide-left");

        controlButton.setOnMouseReleased(actionEvent -> {
            // create an animation to hide sidebar.
            final Animation hideSidebar = new Transition() {
                { setCycleDuration(Duration.millis(250)); }
                protected void interpolate(double frac) {
                    final double curWidth = width * (1.0 - frac);
                    setPrefWidth(curWidth);
                    setTranslateX(-(-width + curWidth));
                }
            };
            hideSidebar.onFinishedProperty().set(actionEvent1 -> {
                setVisible(false);
                controlButton.getStyleClass().remove("hide-left");
                controlButton.getStyleClass().add("show-right");
            });

            // create an animation to show a sidebar.
            final Animation showSidebar = new Transition() {
                { setCycleDuration(Duration.millis(250)); }
                protected void interpolate(double frac) {
                    final double curWidth = width * frac;
                    setPrefWidth(curWidth);
                    setTranslateX(-(-width + curWidth));
                }
            };
            showSidebar.onFinishedProperty().set(actionEvent12 -> {
                controlButton.getStyleClass().add("hide-left");
                controlButton.getStyleClass().remove("show-right");
            });

            if (showSidebar.statusProperty().get() == Animation.Status.STOPPED && hideSidebar.statusProperty().get() == Animation.Status.STOPPED) {
                if (isVisible()) {
                    hideSidebar.play();
                } else {
                    setVisible(true);
                    showSidebar.play();
                }
            }
        });
    }
}