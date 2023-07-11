package net.csdn.davinci.core.bindingadapter;

import android.view.View;
import android.widget.RelativeLayout;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class DavinciViewBindingAdapter {

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
    @BindingAdapter("davinci_bind_isGone")
    public static void isGone(View view, boolean isGone) {
        view.setVisibility(isGone ? View.GONE : View.VISIBLE);
    }

    /**
     * 是否选中
     */
    @BindingAdapter("davinci_bind_isSelected")
    public static void isSelected(View view, boolean isSelected) {
        view.setSelected(isSelected);
    }
}
