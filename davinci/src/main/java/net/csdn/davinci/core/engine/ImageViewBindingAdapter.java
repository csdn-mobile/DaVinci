package net.csdn.davinci.core.engine;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

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

}
