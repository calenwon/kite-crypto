package com.kite.utils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * File tools
 *
 * @author Calendar
 */
public class FileUtil {

    public static String replaceExtension(String filePath, String newExtension) {
        String newFilePath;
        int index = filePath.lastIndexOf(".");
        if(index < 0) {
            newFilePath = filePath + newExtension;
        } else {
            newFilePath = filePath.substring(0, index) + newExtension;
        }

        if(new File(newFilePath).exists()) {
            newFilePath = newFilePath.replace(newExtension, "") + "-" + (System.currentTimeMillis() / 1000) + newExtension;
        }

        return newFilePath;
    }

    public static String getFileExtension(String filePath) {
        int index = filePath.lastIndexOf(".");
        if(index < 0) {
            return "";
        }

        return filePath.substring(index);
    }

    public static boolean checkCompressFile(String fileExtension) {

        Set<String> set = new HashSet<>();
        set.add(".7z");
        set.add(".bz2");
        set.add(".bzip2");
        set.add(".cab");
        set.add(".deb");
        set.add(".emz");
        set.add(".gz");
        set.add(".gzip");
        set.add(".isz");
        set.add(".kz");
        set.add(".pkg");
        set.add(".rar");
        set.add(".sitx");
        set.add(".tar");
        set.add(".tar.bz2");
        set.add(".tar.gz");
        set.add(".tgz");
        set.add(".txz");
        set.add(".tz");
        set.add(".xz");
        set.add(".z");
        set.add(".zip");
        set.add(".zipx");
        set.add(".mp4");
        set.add(".mkv");
        set.add(".mp3");
        set.add(".flac");
        set.add(".wav");
        set.add(".png");
        set.add(".gif");
        set.add(".jpeg");
        set.add(".webp");

        return !set.contains(fileExtension);
    }

}
