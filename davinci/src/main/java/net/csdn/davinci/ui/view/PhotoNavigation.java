package net.csdn.davinci.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import net.csdn.davinci.R;
import net.csdn.davinci.databinding.DavinciViewPhotoNavigationBinding;

public class PhotoNavigation extends RelativeLayout {

    private DavinciViewPhotoNavigationBinding mBinding;

    public PhotoNavigation(Context context) {
        this(context, null);
    }

    public PhotoNavigation(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoNavigation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mBinding = DavinciViewPhotoNavigationBinding.inflate(LayoutInflater.from(context), this, true);
    }

    /**
     * 设置标题
     */
    public void setTitle(String title) {
        mBinding.setTitle(title);
    }

    /**
     * 设置向上箭头
     */
    public void setArrowUp() {
        mBinding.ivArrow.setImageResource(R.drawable.davinci_arrow_up);
    }

    /**
     * 设置向下箭头
     */
    public void setArrowDown() {
        mBinding.ivArrow.setImageResource(R.drawable.davinci_arrow_down);
    }

    /**
     * 设置点击事件
     */
    public void setListener(OnClickListener onBackClick, OnClickListener onTitleClick) {
        mBinding.setOnBackClick(onBackClick);
        mBinding.setOnTitleClick(onTitleClick);
    }
}
