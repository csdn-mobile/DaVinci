package net.csdn.davinci.utils;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import net.csdn.davinci.Config;
import net.csdn.davinci.R;

public class ImageShowUtils {
    public static void loadThumbnail(@NonNull Context context, int imageWidth, @NonNull ImageView iv, @NonNull String urlPath) {
        Config.imageEngine.loadThumbnail(
                context,
                imageWidth,
                Config.isDayStyle ? R.color.davinci_camera_bg : R.color.davinci_camera_bg_night,
                iv,
                urlPath);
    }
}
