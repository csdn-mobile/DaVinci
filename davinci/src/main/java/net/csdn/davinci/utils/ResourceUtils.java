package net.csdn.davinci.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.TypedValue;

public class ResourceUtils {

    public static int getColorFromAttr(Context context, int attr) {
        if (context == null) {
            return attr;
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, typedValue, true);
        return context.getResources().getColor(typedValue.resourceId);
    }

    public static int getResourcesIdFromAttr(Context context, int attr) {
        if (context == null) {
            return attr;
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.resourceId;
    }

    //获取方法
    public static ColorStateList getFABColorStateList(Context context, int colorRes) {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_enabled}, // enabled
                new int[]{-android.R.attr.state_enabled}, // disabled
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{android.R.attr.state_pressed}  // pressed
        };
        int color = getColorFromAttr(context, colorRes);
        int[] colors = new int[]{color, color, color, color};
        return new ColorStateList(states, colors);
    }
}
