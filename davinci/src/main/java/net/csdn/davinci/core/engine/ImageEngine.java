package net.csdn.davinci.core.engine;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.request.RequestListener;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

public interface ImageEngine {

    /**
     * 读取缩略图（资源uri）
     */
    void loadThumbnail(Context context, int resize, int placeResource, ImageView imageView, String uriPath);

    /**
     * 读取缩略图（path）
     */
    void loadThumbnail(Context context, int resize, Drawable placeDrawable, ImageView imageView, String path);

    /**
     * 读取本地图片
     */
    void loadLocalImage(Context context, int resizeX, int resizeY, ImageView imageView, String path);

    /**
     * 读取本地图片（SubsamplingScaleImageView）
     */
    void loadLocalLongImage(SubsamplingScaleImageView imageView, String path);

    /**
     * 读取网络图片
     */
    void loadNetImage(Context context, ImageView imageView, String path, @Nullable RequestListener<Drawable> listener);

    /**
     * 读取网络图片（SubsamplingScaleImageView）
     */
    void loadNetLongImage(Context context, String path, PhotoViewTarget target);
}
