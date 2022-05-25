package net.csdn.mvvm.databinding;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

/**
 * ImageView的自定义属性
 *
 * @author by KG on 2022/05/16
 */
public class ImageViewBindingAdapter {

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
