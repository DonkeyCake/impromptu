package com.promptu.database;

import com.promptu.FingerprintException;
import com.promptu.serialization.SerializationManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Guy on 18/11/2016.
 */
public class LocalDatabase {

    protected DataSet dataSet;

    private static LocalDatabase instance;
    public static LocalDatabase instance() {
        if(instance == null) instance = new LocalDatabase();
        return instance;
    }

    private LocalDatabase() {
        dataSet = new DataSet();
    }


    public Set<MarkerPoint> getMarkers(String fingerprint) {
        return dataSet.markers.stream()
                .filter(m -> m.mbid.contentEquals(fingerprint))
                .collect(Collectors.toSet());
    }

    public Set<Helper> getHelpers(MarkerPoint marker) {
        return dataSet.helpers.stream()
                .filter(h -> h.uid.contentEquals(marker.uid))
                .collect(Collectors.toSet());
    }

    public Map<MarkerPoint, Set<Helper>> getHelperTree(String fingerprint) {
        Set<MarkerPoint> markers = getMarkers(fingerprint);
        Map<MarkerPoint, Set<Helper>> out = new HashMap<>();
        markers.forEach(m -> out.put(m, getHelpers(m)));
        return out;
    }

    public void addMarker(MarkerPoint marker) {
        dataSet.markers.add(marker);
    }

    public void outputToFile(String filePath) {
        Path path = new File(filePath).toPath();
        SerializationManager.instance().toFile(path, dataSet);
    }

    public void populateFromFile(String filePath) {
        Path path = new File(filePath).toPath();
        if(!Files.exists(path)) {
            System.out.println(filePath+" not found");
            return;
        }
        try {
            DataSet dataSet = SerializationManager.instance().fromFile(path, DataSet.class);
            this.dataSet.clear().importFromOther(dataSet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class TrackInfo {
        protected String fingerprint;
        protected String waveformPath;
        protected Set<MarkerPoint> markers;

        public TrackInfo(String fingerprint, String waveformPath, Set<MarkerPoint> markers) {
            this.fingerprint = fingerprint;
            this.waveformPath = waveformPath;
            this.markers = markers;
        }

        public static TrackInfo fromFingerprint(String fingerprint) throws FingerprintException {
            LocalDatabase db = LocalDatabase.instance();
            return new TrackInfo(fingerprint, db.dataSet.fingerprintWaveform, db.getMarkers(fingerprint));
        }
    }

    public static class DataSet {

        private String trackName;
        private String artist;
        private Set<MarkerPoint> markers;
        private Set<Helper> helpers;
        private String fingerprintWaveform;
        private float duration;
        private String trackPath;

        public DataSet() {
            markers = new LinkedHashSet<>();
            helpers = new LinkedHashSet<>();
            fingerprintWaveform = "";
            trackName = "";
            artist = "";
            trackPath = "";
            duration = 1000;
        }

        public String getTrackName() { return trackName; }
        public void setTrackName(String trackName) { this.trackName = trackName; }

        public String getArtist() { return artist; }
        public void setArtist(String artist) { this.artist = artist; }

        public String getFingerprintWaveform() { return this.fingerprintWaveform; }
        public void setFingerprintWaveform(String fingerprintWaveform) { this.fingerprintWaveform = fingerprintWaveform; }

        public Set<MarkerPoint> getMarkers() {
            return markers;
        }

        public void setMarkers(Set<MarkerPoint> markers) {
            this.markers = markers;
        }

        public Set<Helper> getHelpers() { return helpers; }

        public void setHelpers(Set<Helper> helpers) { this.helpers = helpers; }


        public DataSet clear() {
            this.markers.clear();
            this.fingerprintWaveform = "";
            return this;
        }

        public DataSet importFromOther(DataSet other) {
            other.markers.forEach(this.markers::add);
            return this;
        }

        public void setDuration(float duration) {
            this.duration = duration;
        }

        public float getDuration() {
            return duration;
        }

        public String getTrackPath() {
            return trackPath;
        }

        public void setTrackPath(String trackPath) {
            this.trackPath = trackPath;
        }

        public void prepare() {
            fingerprintWaveform = normalize(fingerprintWaveform);
            trackPath = normalize(trackPath);
        }

        private static String normalize(String path) {
            File base = new File("./");
            return base.toURI().relativize(new File(path).toURI()).getPath();
        }

    }

}
