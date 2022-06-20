package net.csdn.davinci.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import net.csdn.statusbar.StatusBar;

import net.csdn.davinci.Config;
import net.csdn.davinci.databinding.ViewPreviewNavigationBinding;

import java.util.ArrayList;

public class PreviewNavigation extends RelativeLayout {

    private ViewPreviewNavigationBinding mBinding;

    private OnClickListener mOnConfirmClickListener;

    public PreviewNavigation(Context context) {
        this(context, null);
    }

    public PreviewNavigation(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PreviewNavigation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mBinding = ViewPreviewNavigationBinding.inflate(LayoutInflater.from(context), this, true);
        mBinding.setStatusBarHeight(StatusBar.getHeight(context));
        mBinding.setOnConfirmClick(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Config.selectedPhotos == null || Config.selectedPhotos.size() <= 0) {
                    Config.selectedPhotos = new ArrayList<>();
                    Config.selectedPhotos.add(Config.currentPath);
                }

                if (Config.selectedPhotos.size() > 0 && mOnConfirmClickListener != null) {
                    mOnConfirmClickListener.onClick(v);
                }
            }
        });
    }

    public void setListener(OnClickListener onBackClick, OnClickListener onConfirmClick) {
        mBinding.setOnBackClick(onBackClick);
        mOnConfirmClickListener = onConfirmClick;
    }

    /**
     * 动画显示导航
     */
    public void show() {
        if (getVisibility() == VISIBLE) {
            return;
        }
        setVisibility(VISIBLE);

        TranslateAnimation anim = new TranslateAnimation(1, 0, 1, 0, 1, -1, 1, 0);
        anim.setDuration(200);//动画持续的时间为200ms
        anim.setFillEnabled(true);//使其可以填充效果从而不回到原地
        anim.setFillAfter(true);//不回到起始位置
        startAnimation(anim);
    }

    /**
     * 动画隐藏导航
     */
    public void dismiss() {
        if (getVisibility() == GONE) {
            return;
        }

        TranslateAnimation anim = new TranslateAnimation(1, 0, 1, 0, 1, 0, 1, -1);
        anim.setDuration(200);//动画持续的时间为200ms
        anim.setFillEnabled(true);//使其可以填充效果从而不回到原地
        anim.setFillAfter(true);//不回到起始位置
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(anim);
    }
}
