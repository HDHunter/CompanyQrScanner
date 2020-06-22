package com.hunter.appinfomonitor.network.okbizfile;

import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.Locale;

/**
 * @author evan_wang <zgtjwyftc@gmail.com>
 * @version 1.0
 * @since 2/15/17
 */

public class FileUtils {
    private FileUtils() {

    }

    private static String getSuffix(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        String fileName = file.getName();
        if (fileName.equals("") || fileName.endsWith(".")) {
            return null;
        }
        int index = fileName.lastIndexOf('.');
        if (index != -1) {
            return fileName.substring(index + 1).toLowerCase(Locale.US);
        } else {
            return null;
        }
    }

    public static String getMimeType(File file) {
        String suffix = getSuffix(file);
        if (suffix == null) {
            return "file/*";
        }
        String type = getMimeTypeFromExtension(suffix);
        if (!TextUtils.isEmpty(type)) {
            return type;
        }
        return "file/*";

    }

    public static String getMimeTypeFromExtension(String suffix) {
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
    }

}