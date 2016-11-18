package com.promptu;

/**
 * Created by Guy on 18/11/2016.
 */
public class FingerprintException extends Exception {

    public FingerprintException() {
    }

    public FingerprintException(String fingerprint) {
        super("Fingerprint error found with \""+fingerprint+"\"");
    }

    public FingerprintException(String message, Throwable cause) {
        super(message, cause);
    }

    public FingerprintException(Throwable cause) {
        super(cause);
    }

    public FingerprintException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
