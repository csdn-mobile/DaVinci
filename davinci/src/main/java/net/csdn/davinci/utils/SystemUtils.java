package net.csdn.davinci.utils;

import android.content.Context;
import android.os.Build;

public class SystemUtils {
    /**
     * 是否版本低于Android Q
     */
    public static boolean underAndroidQ() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.Q;
    }

    /**
     * 屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 屏幕高度
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}
