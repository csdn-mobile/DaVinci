package net.csdn.davinci.core.engine;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
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
     * 读取图片
     */
    @Override
    public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, String path) {
        Glide.with(context)
                .load(path)
                .apply(new RequestOptions()
                        .override(resizeX, resizeY)
                        .priority(Priority.HIGH)
                        .fitCenter())
                .into(imageView);
    }

    /**
     * 读取GIF图片
     */
    @Override
    public void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, String path) {
        Glide.with(context)
                .asGif()
                .load(path)
                .apply(new RequestOptions()
                        .override(resizeX, resizeY)
                        .priority(Priority.HIGH)
                        .fitCenter())
                .into(imageView);
    }
}
