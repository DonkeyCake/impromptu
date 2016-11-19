package com.promptu.adhoc;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Guy on 19/11/2016.
 */
public class PlaySound {

    private final int BUFFER_SIZE = 128000;
    private File soundFile;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceLine;

    private static PlaySound instance;
    public static PlaySound instance() {
        if(instance == null) instance = new PlaySound();
        return instance;
    }

    Thread thread;

    public void playSound(String filename) {
        thread = new Thread(() -> playSound_impl(filename));
        thread.setDaemon(true);
        thread.start();
    }

    public void stopSound() {
        if(thread != null) {
            thread.stop();
            thread = null;
        }
    }

    /**
     * @param filename the name of the file that is going to be played
     */
    public void playSound_impl(String filename){
        String strFilename = filename;

        try {
            soundFile = new File(strFilename);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            audioStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        audioFormat = audioStream.getFormat();

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        sourceLine.start();

        int nBytesRead = 0;
        byte[] abData = new byte[BUFFER_SIZE];
        while (nBytesRead != -1) {
            try {
                nBytesRead = audioStream.read(abData, 0, abData.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nBytesRead >= 0) {
                @SuppressWarnings("unused")
                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
            }
        }

        sourceLine.drain();
        sourceLine.close();
    }
}
