package net.csdn.davinci.core.bindingadapter;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import net.csdn.davinci.Config;

/**
 * ImageView的自定义属性
 *
 * @author by KG on 2022/05/16
 */
public class ImageViewBindingAdapter {

    /**
     * 读取缩略图
     */
    @BindingAdapter(value = {"davinci_thumbnail_resize", "davinci_thumbnail_place", "davinci_thumbnail_path"})
    public static void loadThumbnail(ImageView imageView, int resize, Drawable placeholder, String path) {
        Config.imageEngine.loadThumbnail(imageView.getContext(), resize, placeholder, imageView, path);
    }

    /**
     * Glide展示网络图片
     */
    @BindingAdapter(value = {"bind_imageUrl", "bind_placeholder", "bind_error"}, requireAll = false)
    public static void loadNetImage(ImageView imageView, String imageUrl, Drawable placeholder, Drawable error) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .placeholder(placeholder)
                .error(error)
                .into(imageView);
    }

}
