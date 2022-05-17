package net.csdn.davinci.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class SystemUtils {
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

    /**
     * 获取APP名称
     */
    public static String getAppName(Context context) {
        String name = "";
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getApplicationInfo().packageName, 0);
            name = (String) (packageManager.getApplicationLabel(applicationInfo));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * Activity是否存活
     */
    public static boolean isActivityRunning(Activity activity) {
        return activity != null && !activity.isDestroyed() && !activity.isFinishing();
    }
}
