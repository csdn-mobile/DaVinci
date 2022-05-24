package net.csdn.davinci.core.bindingadapter;

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
}
