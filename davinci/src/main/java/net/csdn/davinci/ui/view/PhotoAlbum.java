package net.csdn.davinci.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.csdn.davinci.R;

public class PhotoAlbum extends RelativeLayout {

    private RecyclerView rvDirs;
    private View viewBlank;

    public PhotoAlbum(Context context) {
        this(context, null);
    }

    public PhotoAlbum(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoAlbum(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_photo_album, this);

        rvDirs = view.findViewById(R.id.rv_dirs);
        viewBlank = view.findViewById(R.id.view_blank);

        rvDirs.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        viewBlank.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAlbum();
            }
        });
    }

    /**
     * 打开相簿
     */
    public void openAlbum() {
        if (rvDirs == null || getVisibility() == View.VISIBLE) {
            return;
        }
        setVisibility(View.VISIBLE);
        Animation translateAnimation = new TranslateAnimation(1, 0, 1, 0, 1, -1, 1, 0);//平移动画
        translateAnimation.setDuration(150);//动画持续的时间为300ms
        translateAnimation.setFillEnabled(true);//使其可以填充效果从而不回到原地
        translateAnimation.setFillAfter(true);//不回到起始位置
        //如果不添加setFillEnabled和setFillAfter则动画执行结束后会自动回到远点
        rvDirs.startAnimation(translateAnimation);//给imageView添加的动画效果
    }

    /**
     * 关上相簿
     */
    public void closeAlbum() {
        if (rvDirs == null || getVisibility() == View.GONE) {
            return;
        }
        Animation translateAnimation = new TranslateAnimation(1, 0, 1, 0, 1, 0, 1, -1);//平移动画
        translateAnimation.setDuration(150);//动画持续的时间为200ms
        translateAnimation.setFillEnabled(true);//使其可以填充效果从而不回到原地
        translateAnimation.setFillAfter(true);//不回到起始位置
        //如果不添加setFillEnabled和setFillAfter则动画执行结束后会自动回到远点
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
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
        rvDirs.startAnimation(translateAnimation);//给imageView添加的动画效果
    }
}
