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
        dataSet.markers = new LinkedHashSet<>();
        dataSet.fingerprintWaveform = new HashMap<>();
    }

    public String getWaveformPath(String fingerprint) throws FingerprintException {
        if(!dataSet.fingerprintWaveform.containsKey(fingerprint)) throw new FingerprintException(fingerprint);
        return dataSet.fingerprintWaveform.get(fingerprint);
    }

    public Set<MarkerPoint> getMarkers(String fingerprint) {
        return dataSet.markers.stream()
                .filter(m -> m.mbid.contentEquals(fingerprint))
                .collect(Collectors.toSet());
    }

    public void addMarker(MarkerPoint marker) {
        dataSet.markers.add(marker);
    }

    public void addWaveform(String fingerprint, String waveform) {
        dataSet.fingerprintWaveform.put(fingerprint, waveform);
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
            String waveform = db.getWaveformPath(fingerprint);
            return new TrackInfo(fingerprint,
                    waveform, db.getMarkers(fingerprint));
        }
    }

    public static class DataSet {

        private Set<MarkerPoint> markers;
        private Map<String, String> fingerprintWaveform;

        public Set<MarkerPoint> getMarkers() {
            return markers;
        }

        public void setMarkers(Set<MarkerPoint> markers) {
            this.markers = markers;
        }

        public Map<String, String> getFingerprintWaveform() {
            return fingerprintWaveform;
        }

        public void setFingerprintWaveform(Map<String, String> fingerprintWaveform) {
            this.fingerprintWaveform = fingerprintWaveform;
        }

        public DataSet clear() {
            this.markers.clear();
            this.fingerprintWaveform.clear();
            return this;
        }

        public DataSet importFromOther(DataSet other) {
            other.markers.forEach(this.markers::add);
            other.fingerprintWaveform.forEach(this.fingerprintWaveform::put);
            return this;
        }
    }

}
