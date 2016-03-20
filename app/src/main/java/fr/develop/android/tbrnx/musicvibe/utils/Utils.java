package fr.develop.android.tbrnx.musicvibe.utils;

import android.webkit.MimeTypeMap;

import java.text.Normalizer;

/**
 * Created by tbrnx on 18/03/16.
 */
public class Utils {

    public static final String TRACK = "track";
    public static final String ART_URL = "art_url";

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static String formatToDuration(long duration) {
        long secDuration = duration / 1000;
        if(secDuration < 60){
            return String.format("%s s", String.valueOf(secDuration));
        }else if(secDuration < 3600){
            long mins = secDuration / 60;
            long secs = secDuration - (mins * 60);
            return String.format("%s:%s", String.valueOf(mins), twoDigits(secs));
        }else {
            long mins = secDuration / 60;
            long secs = secDuration - (mins * 60);
            return String.format("%s:%s", String.valueOf(mins), String.valueOf(secs));
        }
    }

    private static String twoDigits(long l) {
        if(l < 10)
            return String.format("0%s",String.valueOf(l));
        return String.valueOf(l);
    }

    public static String flattenToAscii(String string) {
        char[] out = new char[string.length()];
        string = Normalizer.normalize(string, Normalizer.Form.NFD);
        int j = 0;
        for (int i = 0, n = string.length(); i < n; ++i) {
            char c = string.charAt(i);
            if (c <= '\u007F') out[j++] = c;
        }
        return new String(out);
    }
}
