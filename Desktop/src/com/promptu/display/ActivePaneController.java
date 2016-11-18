package com.promptu.display;

import com.promptu.database.MarkerBlock;
import com.promptu.database.MarkerPoint;
import com.promptu.database.MarkerType;
import com.promptu.database.TimeUnit;
import com.promptu.utils.LambdaAnimationTimer;
import com.sun.corba.se.impl.orbutil.graph.Graph;
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
import javafx.scene.paint.Paint;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Guy on 18/11/2016.
 */
public class ActivePaneController implements Initializable {

    private boolean isPlaying;
    public static final String PLAY_ICON = "com/promptu/display/play.png";
    public static final String PAUSE_ICON = "com/promptu/display/pause.png";
    private float stretchScale = 5;
    private float speed = 5f;
    private double markerCutoff = 30;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isPlaying = false;
        testMarkers = new LinkedHashSet<>();
        currentMarkers = new ArrayList<>();

        for(int i = 0; i < 20; i++) {
            MarkerBlock block = new MarkerBlock();
            block.mbid("test"+(i));
            block.startTime().tickIndex((150*i));
            block.endTime().tickIndex(149+(150*i));
            testMarkers.add(block);
        }

        resetTimers();
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
        resetTimers();
        setState(false);
    }

    private void invalidateMarkerDetails() {

    }

    private void setState(boolean newState) {
        isPlaying = newState;

        Image newImg;
        if(isPlaying) newImg = new Image(PAUSE_ICON);
        else newImg = new Image(PLAY_ICON);
        playPauseBtn.setImage(newImg);
    }

    private void resetTimers() {
        currentX = canvasLarge.getWidth();
        fullWidth = canvasLarge.getWidth()*stretchScale;
        currentMarkers.clear();
        currentMarkers.addAll(testMarkers);
        currentMarkers.sort((m1, m2) -> m1.startTime().compareTo(m2.startTime()));
    }

    private void clearCanvas() {
        final GraphicsContext smallContext = canvasSmall.getGraphicsContext2D();
        smallContext.clearRect(0, 0, canvasSmall.getWidth(), canvasSmall.getHeight());
        final GraphicsContext largeContext = canvasLarge.getGraphicsContext2D();
        largeContext.clearRect(0, 0, canvasLarge.getWidth(), canvasLarge.getHeight());
    }

    private void drawCanvasSmall() {
        Image image = testWaveform.getImage();
        final GraphicsContext context = canvasSmall.getGraphicsContext2D();
        context.drawImage(image, 0, 0, canvasSmall.getWidth(), canvasSmall.getHeight());
        context.setStroke(Color.RED);
        double x = percentAcross() * canvasSmall.getWidth();
        context.strokeLine(x, 0, x, canvasSmall.getHeight());
    }
    private void drawCanvasLarge() {
        Image image = testWaveform.getImage();
        final GraphicsContext context = canvasLarge.getGraphicsContext2D();
        context.drawImage(image, currentX, 0, canvasLarge.getWidth()*5, canvasLarge.getHeight());
        drawMarkers(context);
        context.setStroke(Color.RED);
        context.strokeLine(30, 0, 30, canvasLarge.getHeight());
    }
    private void drawMarkers(GraphicsContext context) {
         testMarkers.stream().collect(Collectors.toList()).forEach(marker -> {
            Color col = Color.GRAY;
            int i = getMarkerIndex(marker);
            switch(i) {
                case 0: col = Color.RED; break;
                case 1: col = Color.YELLOW; break;
            }
            boolean isBlock = marker.getType().equals(MarkerType.BLOCK);
            double endPos;
            if(isBlock) endPos = drawBlock(context, (MarkerBlock) marker, col);
            else endPos = drawPoint(context, marker, col);
            if(endPos < markerCutoff) {
                currentMarkers.remove(marker);
                invalidateMarkerDetails();
            }
        });
    }

    private int getMarkerIndex(MarkerPoint marker) {
        return currentMarkers.indexOf(marker);
    }

    private double drawPoint(GraphicsContext context, MarkerPoint point, Color rgb) {
        double x = point.startTime().tickIndex() + currentX;
        context.setStroke(rgb);
        context.strokeLine(x, 0, x, canvasLarge.getHeight());
        return x;
    }
    private double drawBlock(GraphicsContext context, MarkerBlock block, Color rgb) {
        double startX = block.startTime().tickIndex() + currentX;
        double endX = block.endTime().tickIndex() + currentX;
        double height = canvasLarge.getHeight();

        context.setFill(new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), .7));
        context.fillRect(startX, 0, endX - startX, height);
        context.setStroke(rgb);
        context.strokeLine(startX, 0, startX, height);
        context.strokeLine(endX, 0, endX, height);
        return endX;
    }

    // TODO format correctly
    double currentX;
    double fullWidth;
    Set<MarkerPoint> testMarkers;
    List<MarkerPoint> currentMarkers;

    // Continuous rendering, called every frame
    private void draw(long handle) {
//        double delta = (handle - System.currentTimeMillis())/1000;
        clearCanvas();
        drawCanvasSmall();
        drawCanvasLarge();
        if(!isPlaying) return;
        currentX -= speed;
        if(percentAcross() > 1) {
            setState(!isPlaying);
            resetTimers();
        }
    }

    private double percentAcross() {
        return -currentX / fullWidth;
    }

}
