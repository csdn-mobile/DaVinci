package net.csdn.davinci.utils;

import android.content.Context;

public class DensityUtils {

    // 根据手机的分辨率将dp的单位转成px(像素)
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
