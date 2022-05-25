package net.csdn.mvvm.databinding;

import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.BindingAdapter;

/**
 * 所有View的自定义属性
 *
 * @author by KG on 2022/05/16
 */
public class ViewBindingAdapter {

    /**
     * 是否隐藏
     */
    @BindingAdapter("bind_isGone")
    public static void isGone(View view, boolean isGone) {
        view.setVisibility(isGone ? View.GONE : View.VISIBLE);
    }

    /**
     * 是否选中
     */
    @BindingAdapter("bind_isSelected")
    public static void isSelected(View view, boolean isSelected) {
        view.setSelected(isSelected);
    }

    /**
     * 是否选中
     */
    @BindingAdapter("bind_height")
    public static void setHeight(View view, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }
}
