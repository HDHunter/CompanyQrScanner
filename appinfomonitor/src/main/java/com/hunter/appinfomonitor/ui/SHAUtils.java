package com.hunter.appinfomonitor.ui;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAUtils {
    public static String getSHA(String val) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("SHA-1");
            md5.update(val.getBytes());
            byte[] m = md5.digest();//加密
            return getString(m);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getString(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            sb.append(b[i]);
        }
        return sb.toString();
    }
}
