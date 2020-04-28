package com.hunter.appinfomonitor.ui;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author talaya
 */
public class MD5EncodeUtil {
    private final static String[] HEX_DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * convert byte array to hex string
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /**
     * convert byte to hex string
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }

    /**
     * get md5 encode of string
     *
     * @param origin origin
     * @return md5
     */
    public static String MD5Encode(String origin) {
        if (origin == null) {
            return null;
        }
        String resultString;
        resultString = origin;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (md != null) {
            resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
        }
        return resultString;
    }
}