package net.csdn.davinci.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.csdn.statusbar.StatusBar;

import net.csdn.davinci.Config;
import net.csdn.davinci.R;

public class PreviewNavigation extends RelativeLayout {

    private View viewStatusBar;
    private ImageView ivBack;
    private TextView tvConfirm;

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
        View view = LayoutInflater.from(context).inflate(R.layout.view_preview_navigation, this);

        viewStatusBar = view.findViewById(R.id.view_status_bar);
        ivBack = view.findViewById(R.id.iv_back);
        tvConfirm = view.findViewById(R.id.tv_confirm);

        ViewGroup.LayoutParams layoutParams = viewStatusBar.getLayoutParams();
        layoutParams.height = StatusBar.getHeight(context);
        viewStatusBar.setLayoutParams(layoutParams);

        setVisibility(Config.previewSelectable ? VISIBLE : GONE);
    }

    /**
     * 设置返回键点击事件
     */
    public void setOnBackClick(OnClickListener listener) {
        if (ivBack == null || listener == null) {
            return;
        }
        ivBack.setOnClickListener(listener);
    }

    /**
     * 设置确认点击事件
     */
    public void setOnConfirmClick(OnClickListener listener) {
        if (tvConfirm == null || listener == null) {
            return;
        }
        tvConfirm.setOnClickListener(listener);
    }

    /**
     * 动画显示导航
     */
    public void show() {
        if (viewStatusBar == null || getVisibility() == VISIBLE) {
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
        if (viewStatusBar == null || getVisibility() == GONE) {
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
