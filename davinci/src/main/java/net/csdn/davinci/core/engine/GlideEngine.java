package net.csdn.davinci.core.engine;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
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
        if (TextUtils.isEmpty(path)) {
            return;
        }
        if (imageView.getTag() == null || !imageView.getTag().equals(path)) {
            imageView.setTag(path);
            Glide.with(context)
                    .asBitmap()
                    .load(Uri.parse(path))
                    .apply(new RequestOptions()
                            .override(resize, resize)
                            .placeholder(placeResource)
                            .centerCrop()
                            .priority(Priority.LOW))
                    .into(imageView);
        }
    }

    /**
     * 读取本地图片
     */
    @Override
    public void loadLocalImage(Context context, int resizeX, int resizeY, ImageView imageView, String uriPath, boolean isGif) {
        RequestBuilder requestBuilder;
        if (isGif) {
            requestBuilder = Glide.with(context).asGif();
        } else {
            requestBuilder = Glide.with(context).asBitmap();
        }
        requestBuilder
                .load(Uri.parse(uriPath))
                .apply(new RequestOptions()
                        .override(resizeX, resizeY)
                        .priority(Priority.LOW))
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
