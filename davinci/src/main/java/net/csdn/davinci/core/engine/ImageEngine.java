package net.csdn.davinci.core.engine;

import android.content.Context;
import android.widget.ImageView;

public interface ImageEngine {

    /**
     * 读取缩略图
     */
    void loadThumbnail(Context context, int resize, int placeResource, ImageView imageView, String path);

    /**
     * 读取图片
     */
    void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, String path);

    /**
     * 读取GIF图片
     */
    void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, String path);
}
