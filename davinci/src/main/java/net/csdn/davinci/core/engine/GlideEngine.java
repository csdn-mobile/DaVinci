package net.csdn.davinci.core.engine;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

public class GlideEngine implements ImageEngine {

    /**
     * 读取缩略图
     */
    @Override
    public void loadThumbnail(Context context, int resize, int placeResource, ImageView imageView, String path) {
        Glide.with(context)
                .load(path)
                .apply(new RequestOptions()
                        .override(resize, resize)
                        .placeholder(placeResource)
                        .centerCrop())
                .into(imageView);
    }

    /**
     * 读取本地图片
     */
    @Override
    public void loadLocalImage(Context context, int resizeX, int resizeY, ImageView imageView, String path) {
        Glide.with(context)
                .load(path)
                .apply(new RequestOptions()
                        .override(resizeX, resizeY)
                        .priority(Priority.HIGH))
                .into(imageView);
    }

    /**
     * 读取本地长图片
     */
    @Override
    public void loadLocalLongImage(Context context, ImageView imageView, String path) {
        Glide.with(context)
                .load(path)
                .apply(new RequestOptions()
                        .priority(Priority.HIGH)
                        .dontTransform())
                .into(imageView);
    }

    /**
     * 读取网络图片
     */
    @Override
    public void loadNetImage(Context context, ImageView imageView, String path, @Nullable RequestListener<Drawable> listener) {
        Glide.with(context)
                .load(path)
                .addListener(listener)
                .into(imageView);
    }

    /**
     * 读取长图网络图片
     */
    @Override
    public void loadNetLongImage(Context context, ImageView imageView, String path, @Nullable RequestListener<Drawable> listener) {
        Glide.with(context)
                .load(path)
                .addListener(listener)
                .apply(new RequestOptions()
                        .priority(Priority.HIGH)
                        .dontTransform())
                .into(imageView);
    }
}
