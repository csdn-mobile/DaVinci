package net.csdn.davinci.core.engine;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

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
     * 读取网络图片
     */
    @Override
    public void loadNetImage(Context context, ImageView imageView, String path, @Nullable RequestListener<Drawable> listener) {
        Glide.with(context)
                .load(path)
                .addListener(listener)
                .into(imageView);
    }
}
