package net.csdn.davinci.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import net.csdn.davinci.BR;
import net.csdn.davinci.BusEvent;
import net.csdn.davinci.R;
import net.csdn.davinci.core.entity.Album;
import net.csdn.davinci.databinding.ItemAlbumBinding;
import net.csdn.davinci.databinding.ViewPhotoAlbumBinding;
import net.csdn.davinci.ui.viewmodel.AlbumItemViewModel;
import net.csdn.davinci.mvvm.bus.DavinciBus;
import net.csdn.davinci.mvvm.ui.adapter.DavinciOriginAdapter;

import java.util.ArrayList;

public class PhotoAlbum extends RelativeLayout {

    private ViewPhotoAlbumBinding mBinding;
    private DavinciOriginAdapter<Album, ItemAlbumBinding> mAdapter;

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
        mBinding = ViewPhotoAlbumBinding.inflate(LayoutInflater.from(context), this, true);
        mAdapter = new DavinciOriginAdapter<>(R.layout.item_album, BR.viewmodel, AlbumItemViewModel.class, null);
        mBinding.setAdapter(mAdapter);
        mBinding.setOnBlankClick(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DavinciBus.getInstance().with(BusEvent.Photo.ALBUM_BLANK_CLICK).setValue(null);
            }
        });
    }

    /**
     * 打开相簿
     */
    public void openAlbum() {
        if (getVisibility() == View.VISIBLE) {
            return;
        }
        setVisibility(View.VISIBLE);
        Animation translateAnimation = new TranslateAnimation(1, 0, 1, 0, 1, -1, 1, 0);//平移动画
        translateAnimation.setDuration(150);//动画持续的时间为300ms
        translateAnimation.setFillEnabled(true);//使其可以填充效果从而不回到原地
        translateAnimation.setFillAfter(true);//不回到起始位置
        //如果不添加setFillEnabled和setFillAfter则动画执行结束后会自动回到远点
        mBinding.rvDirs.startAnimation(translateAnimation);//给imageView添加的动画效果
    }

    /**
     * 关上相簿
     */
    public void closeAlbum() {
        if (getVisibility() == View.GONE) {
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
        mBinding.rvDirs.startAnimation(translateAnimation);//给imageView添加的动画效果
    }

    /**
     * 设置相册数据
     */
    public void setData(ArrayList<Album> albums) {
        mAdapter.setDatas(albums);
    }
}
