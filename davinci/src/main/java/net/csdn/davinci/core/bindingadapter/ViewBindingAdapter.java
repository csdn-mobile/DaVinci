package net.csdn.davinci.core.bindingadapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class ViewBindingAdapter {

    @BindingAdapter("davinci_rlrv_marginStart")
    public static void marginStart(RelativeLayout rl, int marginStart) {
        if (rl.getLayoutParams() instanceof RecyclerView.LayoutParams) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) rl.getLayoutParams();
            layoutParams.leftMargin = marginStart;
            rl.setLayoutParams(layoutParams);
        }
    }

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
