package net.csdn.davinci.utils;

import android.text.TextUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtils {

    /**
     * 网址校验
     */
    public static boolean checkUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        String parseUrl = "(((http[s]{0,1}|ftp)://)?[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)";
        Pattern pattern = Pattern.compile(parseUrl);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    /**
     * 获取url的host
     */
    public static String getHost(String content) {
        // 获取host
        String host = "";
        try {
            URL url = new URL(content);
            host = url.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return host;
    }
}
