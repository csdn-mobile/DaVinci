package net.csdn.davinci.core.engine;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.request.RequestListener;

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
     * 读取网络图片
     */
    void loadNetImage(Context context, ImageView imageView, String path, @Nullable RequestListener<Drawable> listener);
}
