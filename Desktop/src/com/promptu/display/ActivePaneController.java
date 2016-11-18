package com.promptu.display;

import com.promptu.utils.LambdaAnimationTimer;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Guy on 18/11/2016.
 */
public class ActivePaneController implements Initializable {

    private boolean isPlaying;
    public static final String PLAY_ICON = "com/promptu/display/play.png";
    public static final String PAUSE_ICON = "com/promptu/display/pause.png";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isPlaying = false;
        drawCanvasSmall();
        drawCanvasLarge();
        AnimationTimer timer = new LambdaAnimationTimer(this::draw);
        timer.start();
    }

    @FXML private ImageView playPauseBtn;
    @FXML private ImageView testWaveform;
    @FXML private Canvas canvasLarge;
    @FXML private Canvas canvasSmall;

    @FXML
    public void togglePlay(MouseEvent event) {
        setState(!isPlaying);
    }

    @FXML
    public void toStart(MouseEvent event) {
        currentX = 0;
        setState(false);
    }

    private void setState(boolean newState) {
        isPlaying = newState;

        Image newImg;
        if(isPlaying) newImg = new Image(PAUSE_ICON);
        else newImg = new Image(PLAY_ICON);
        playPauseBtn.setImage(newImg);
    }

    private void drawCanvasSmall() {
        Image image = testWaveform.getImage();
        final GraphicsContext context = canvasSmall.getGraphicsContext2D();
        context.drawImage(image, 0, 0, canvasSmall.getWidth(), canvasSmall.getHeight());
        context.setStroke(Color.RED);
        double x = 0;
        x = (Math.abs(currentX) / fullWidth) * canvasSmall.getWidth();
        context.strokeLine(x, 0, x, canvasSmall.getHeight());
    }
    private void drawCanvasLarge() {
        fullWidth = canvasLarge.getWidth()*5;
        Image image = testWaveform.getImage();
        final GraphicsContext context = canvasLarge.getGraphicsContext2D();
        context.drawImage(image, currentX, 0, canvasLarge.getWidth()*5, canvasLarge.getHeight());
        context.setStroke(Color.RED);
        context.strokeLine(30, 0, 30, canvasLarge.getHeight());
    }

    // TODO format correctly
    double currentX = 0;
    double fullWidth = 100;

    // Continuous rendering, called every frame
    private void draw(long handle) {
//        double delta = (handle - System.currentTimeMillis())/1000;
        drawCanvasSmall();
        drawCanvasLarge();
        if(!isPlaying) return;
        currentX -= 1;
    }

}
