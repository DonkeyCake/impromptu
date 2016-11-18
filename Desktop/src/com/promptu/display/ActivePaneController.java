package com.promptu.display;

import com.promptu.database.*;
import com.promptu.display.sub.HelperPanelController;
import com.promptu.display.sub.MarkerPanelController;
import com.promptu.utils.LambdaAnimationTimer;
import com.promptu.utils.StringGenerator;
import com.sun.corba.se.impl.orbutil.graph.Graph;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private double currentX;
    private double fullWidth;
    private List<MarkerPoint> testMarkers;
    private List<MarkerPoint> currentMarkers;
    private Set<Helper> markerHelpers;
    private Map<MarkerPoint, Map.Entry<Parent, MarkerPanelController>> markerControllers;
    private Map<Helper, Map.Entry<Parent, HelperPanelController>> helperControllers;
    private MarkerPoint selectedMarker;
    private TimeUnit selectedUnit;
    private boolean isDragging = false;
    private boolean canDrag = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isPlaying = false;
        testMarkers = new ArrayList<>();
        currentMarkers = new ArrayList<>();
        markerControllers = new HashMap<>();
        helperControllers = new HashMap<>();
        markerHelpers = new LinkedHashSet<>();

        float index = 0;
//
//        for(int i = 0; i < 20; i++) {
//            MarkerBlock block = new MarkerBlock();
//            block.mbid("test" + (i));
//            block.header("Marker " + i);
//            block.text(StringGenerator.generateString(8));
//            block.startTime().tickIndex(index);
//            block.endTime().tickIndex(index+.02f);
//            index += .05f;
//            System.out.println(index);
//            testMarkers.add(block);
//            addMarker(block);
//        }

        Random random = new Random();

        List<MarkerPoint> collect = testMarkers.stream().collect(Collectors.toList());
//        for(int i = 0; i < 20; i++) {
//            MarkerPoint marker = collect.get(i);
//            for(int j = 0; j < 4; j++) {
//                if(random.nextInt() % 3 == 0) {
//                    Helper helper = new Helper(marker.uid(), StringGenerator.generateString(32));
//                    markerHelpers.add(helper);
//                    addHelper(helper);
//                }
//            }
//        }

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
    @FXML private VBox markerContainer;
    @FXML private VBox helperVBox;

    @FXML
    public void togglePlay(MouseEvent event) {
        setState(!isPlaying);
        if(isPlaying) invalidateMarkerDetails();
    }

    @FXML
    public void toStart(MouseEvent event) {
        resetTimers();
        setState(false);
    }

    @FXML
    public void canvasSmall_onClick(MouseEvent event) {
        double x = event.getX();
        double perc = (x / canvasSmall.getWidth());
        currentX = -(perc * getCanvasWidth());
    }

    @FXML
    public void canvasSmall_onDrag(MouseEvent event) {
        double x = event.getX();
        double perc = (x / canvasSmall.getWidth());
        currentX = -(perc * getCanvasWidth());
    }

    @FXML
    public void canvasLarge_onRClick(ContextMenuEvent event) {
        MarkerBlock block = new MarkerBlock();
        double x = event.getX();
        double modX = (-currentX) + x;
        double perc = (modX / getCanvasWidth());
        block.startTime().tickIndex((float) perc);
        block.endTime().tickIndex((float) perc + 0.05f);
        block.header("Header");
        block.text("Text");
        testMarkers.add(block);

        addMarker(block);
    }
    @FXML
    public void canvasLarge_onClick(MouseEvent event) {
        if(isDragging) return;
        double x = event.getX();
        double modX = (-currentX) + x;
        double threshold = 5;

        List<TimeUnit> units = new ArrayList<>();
        testMarkers.forEach(marker -> {
            units.add(marker.startTime());
            if(marker instanceof MarkerBlock)
                units.add(((MarkerBlock) marker).endTime());
        });
        Stream<TimeUnit> sorted = units.stream().filter(unit -> {
            double v = unit.tickIndex();
            v = v * getCanvasWidth();
            double min = (modX - threshold);
            double max = (modX + threshold);
            return v > min && v < max;
        }).sorted((a, b) -> {
            double v = a.tickIndex();
            double w = b.tickIndex();
            if (v > w) return 1;
            if (v < w) return -1;
            return 0;
        });
        Optional<TimeUnit> first = sorted.findFirst();
        if (!first.isPresent()) return;
        selectedUnit = first.get();
    }
    @FXML
    public void canvasLarge_drag(MouseEvent event) {
        if(selectedUnit == null) return;
        isDragging = true;
        double x = event.getX();
        double modX = (-currentX) + x;
        double perc = modX / getCanvasWidth();
        selectedUnit.tickIndex((float) perc);
    }
    @FXML
    public void canvasLarge_dragEnd(MouseEvent event) {
        if(!isDragging) return;
        isDragging = false;
        selectedUnit = null;
    }

    @FXML
    public void canvasLarge_selectMarker(MouseEvent event) {
        float x = (float) event.getX();
        double modX = (-currentX) + x;
        final double perc = modX / getCanvasWidth();
        Optional<MarkerBlock> first = testMarkers.stream()
                .filter(m -> m.getType().equals(MarkerType.BLOCK))
                .map(m -> (MarkerBlock) m)
                .filter(m -> {
                    float a = m.startTime().tickIndex();
                    float b = m.endTime().tickIndex();
                    return perc > a && perc < b;
                }).findFirst();
        if(!first.isPresent()) {
            selectedMarker = null;
            invalidateMarkerDetails();
            return;
        }
        selectedMarker = first.get();
        invalidateMarkerDetails();
    }

    private void loadMarkerDetails() {
        if(selectedMarker == null) return;
        if(!markerControllers.containsKey(selectedMarker)) return;
        Map.Entry<Parent, MarkerPanelController> entry = markerControllers.get(selectedMarker);
        markerContainer.getChildren().clear();
        markerContainer.getChildren().add(entry.getKey());
        entry.getKey().getStyleClass().clear();
        entry.getKey().getStyleClass().add("marker_selected");
    }

    private void addMarker(MarkerPoint point) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sub/markerPanel.fxml"));
            Parent root = loader.load();
            MarkerPanelController controller = loader.getController();
            controller.setMarker(point);
            markerControllers.put(point, new AbstractMap.SimpleEntry<>(root, controller));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addHelper(Helper helper) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sub/helperPanel.fxml"));
            Parent root = loader.load();
            helperControllers.put(helper, new AbstractMap.SimpleEntry<>(root, loader.getController()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void invalidateMarkerDetails() {
        markerContainer.getChildren().clear();
        final int[] i = {0};
        currentMarkers.forEach(marker -> {
            Map.Entry<Parent, MarkerPanelController> entry = markerControllers.get(marker);
            entry.getValue().setHeader(marker.header());
            entry.getValue().setText(marker.text());
            entry.getKey().minWidth(markerContainer.getWidth());
            entry.getKey().prefWidth(markerContainer.getWidth());
            entry.getKey().maxWidth(markerContainer.getWidth());
            if(i[0] < 2) entry.getValue().setClass("marker_"+ (i[0]));
            else entry.getValue().setClass("marker");
            i[0]++;
            markerContainer.getChildren().add(entry.getKey());
        });
        loadMarkerDetails();
//        invalidateHelperDetails();
    }

    private void invalidateHelperDetails() {
        helperVBox.getChildren().clear();
        if(currentMarkers.size() <= 0) return;
        MarkerPoint active = currentMarkers.get(0);
        markerHelpers.stream()
                .filter(h -> h.getUid().contentEquals(active.uid()))
                .forEach(h -> {
                    Map.Entry<Parent, HelperPanelController> entry = helperControllers.get(h);
                    entry.getValue().setText(h.getText());
                    helperVBox.getChildren().add(entry.getKey());
                });
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
        double x = (point.startTime().tickIndex() * getCanvasWidth()) + currentX;
        context.setStroke(rgb);
        context.strokeLine(x, 0, x, canvasLarge.getHeight());
        return x;
    }
    private double drawBlock(GraphicsContext context, MarkerBlock block, Color rgb) {
        double startX = (block.startTime().tickIndex() * getCanvasWidth()) + currentX;
        double endX = (block.endTime().tickIndex() * getCanvasWidth()) + currentX;
        double height = canvasLarge.getHeight();

        context.setFill(new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), .7));
        context.fillRect(startX, 0, endX - startX, height);
        context.setStroke(rgb);
        context.strokeLine(startX, 0, startX, height);
        context.strokeLine(endX, 0, endX, height);
        return endX;
    }

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

    private double getCanvasWidth() {
        return canvasLarge.getWidth() * stretchScale;
    }

    private double percentAcross() {
        return -currentX / fullWidth;
    }

}
