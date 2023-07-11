package net.csdn.davinci.core.bindingadapter;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import net.csdn.davinci.utils.ImageShowUtils;

/**
 * ImageView的自定义属性
 *
 * @author by KG on 2022/05/16
 */
public class DavinciImageViewBindingAdapter {

    /**
     * 读取缩略图
     */
    @BindingAdapter(value = {"davinci_thumbnail_resize", "davinci_thumbnail_path"})
    public static void loadThumbnail(ImageView imageView, int resize, String path) {
        ImageShowUtils.loadThumbnail(imageView.getContext(), resize, imageView, path);
    }
}
