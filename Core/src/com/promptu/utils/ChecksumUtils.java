package com.promptu.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * Created by Guy on 08/01/2017.
 */
public class ChecksumUtils {

    public static byte[] createChecksum(String filename) throws Exception {
        InputStream fis =  new FileInputStream(filename);

        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;

        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        fis.close();
        return complete.digest();
    }

    // see this How-to for a faster way to convert
    // a byte array to a HEX string
    public static String getMD5Checksum(String filename) throws Exception {
        byte[] b = createChecksum(filename);
        return convertBytesToString(b);
    }

    public static String convertBytesToString(byte[] b) {
        String result = "";
        for (byte aB : b) result += Integer.toString((aB & 0xff) + 0x100, 16).substring(1);
        return result;
    }

}
