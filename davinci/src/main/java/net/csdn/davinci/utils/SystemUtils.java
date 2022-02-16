package net.csdn.davinci.utils;

import android.os.Build;

public class SystemUtils {
    /**
     * 是否版本低于Android Q
     */
    public static boolean underAndroidQ() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.Q;
    }
}
