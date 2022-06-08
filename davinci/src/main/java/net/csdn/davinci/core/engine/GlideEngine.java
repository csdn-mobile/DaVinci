package net.csdn.davinci.core.engine;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

public class GlideEngine implements ImageEngine {

    /**
     * 读取缩略图（资源ID）
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
     * 读取缩略图（Drawable）
     */
    @Override
    public void loadThumbnail(Context context, int resize, Drawable placeDrawable, ImageView imageView, String path) {
        Glide.with(context)
                .load(path)
                .apply(new RequestOptions()
                        .override(resize, resize)
                        .placeholder(placeDrawable)
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

    @Override
    public void loadLocalLongImage(SubsamplingScaleImageView imageView, String path) {
        if (imageView == null) {
            return;
        }
        imageView.setOnImageEventListener(new ImageEventListener(imageView));
        imageView.setImage(ImageSource.uri(path));
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

    @Override
    public void loadNetLongImage(Context context, String path, PhotoViewTarget target) {
        if (target == null) {
            return;
        }
        Glide.with(context)
                .download(new GlideUrl(path))
                .into(target);
    }
}
