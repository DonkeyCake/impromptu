package com.promptu;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowAdapter;
import com.promptu.concurrency.AtomicFloat;
import com.promptu.event.events.CloseRequestEvent;
import com.promptu.gl.GLLauncher;
import com.promptu.tween.AtomicFloatTweenAccessor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

/**
 * Created by Guy on 18/11/2016.
 */
public class DesktopLauncher extends Application {

    public static void main(String[] args) {
        boolean useOpenGL = false;
        for (int i = 0; i < args.length; i++) {
            if(args[i].equalsIgnoreCase("--OpenGL")) useOpenGL = true;
            if(args[i].equalsIgnoreCase("-gl")) useOpenGL = true;
            if(args[i].equalsIgnoreCase("--scale")) handleScreenScale(args, i);
            if(args[i].equalsIgnoreCase("-s")) handleScreenScale(args, i);
        }

        tweenManager = new TweenManager();
        Tween.registerAccessor(AtomicFloat.class, new AtomicFloatTweenAccessor());

        if(useOpenGL) startGL(args);
        else startFX(args);
    }

    private static void handleScreenScale(String[] args, int i) {
        i+=1;
        if(i > args.length-1) return;
        try{
            float v = Float.parseFloat(args[i]);
            screenScale = v;
        }catch (Exception ignored) {}
    }

    public static TweenManager tweenManager;

    private static float screenScale = 0.75f;

    private static void startGL(String[] args) {

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode displayMode = gd.getDisplayMode();

        int width = (int) (displayMode.getWidth() * screenScale);
        int height = (int) (displayMode.getHeight() * screenScale);

//        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
//        cfg.title = "Promptu";
//        cfg.width = width;
//        cfg.height = height;
//        new LwjglApplication(new GLLauncher(), cfg);

        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setWindowListener(new Lwjgl3WindowAdapter(){
            @Override
            public boolean closeRequested() {
                new CloseRequestEvent().fireImmediate(this);
                return super.closeRequested();
            }
        });
        cfg.useVsync(false);
        cfg.setTitle("Promptu");
        cfg.setWindowedMode(width, height);
        new Lwjgl3Application(new GLLauncher(), cfg);
    }
    private static void startFX(String[] args) {
        launch(DesktopLauncher.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Active Pane");
        primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("fx/display/activePane.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("fx/display/activePane.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
