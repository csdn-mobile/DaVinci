package net.csdn.davinci.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.Toast;

import net.csdn.davinci.Config;
import net.csdn.davinci.R;
import net.csdn.davinci.databinding.ViewPreviewBottomBarBinding;
import net.csdn.davinci.ui.adapter.PreviewSelectedAdapter;

public class PreviewBottomBar extends LinearLayout {

    private ViewPreviewBottomBarBinding mBinding;
    private PreviewSelectedAdapter mAdapter;

    public PreviewBottomBar(Context context) {
        this(context, null);
    }

    public PreviewBottomBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PreviewBottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mBinding = ViewPreviewBottomBarBinding.inflate(LayoutInflater.from(context), this, true);
        mAdapter = new PreviewSelectedAdapter(Config.selectedPhotos);
        mBinding.setAdapter(mAdapter);
        notifyDataSetChanged();

        setListener();
    }

    private void setListener() {
        mBinding.rlSelected.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mBinding.tvSelected.isSelected() && Config.selectedPhotos.size() >= Config.maxSelectable) {
                    Toast.makeText(getContext(), getContext().getString(R.string.davinci_over_max_count_tips, Config.maxSelectable), Toast.LENGTH_SHORT).show();
                    return;
                }
                view.setSelected(!view.isSelected());
                if (view.isSelected()) {
                    // 选中图片
                    Config.selectedPhotos.add(Config.currentPath);
                    mBinding.tvSelected.setText(String.valueOf(Config.selectedPhotos.indexOf(Config.currentPath) + 1));
                } else {
                    // 取消选中
                    Config.selectedPhotos.remove(Config.currentPath);
                    mBinding.tvSelected.setText("");
                }
                if (mAdapter != null) {
                    mAdapter.setSelectedItem(mBinding.rv, Config.selectedPhotos.indexOf(Config.currentPath));
                    mAdapter.setDatas(Config.selectedPhotos);
                }
            }
        });
    }

    /**
     * 动画显示
     */
    public void show() {
        if (!Config.previewSelectable || getVisibility() == VISIBLE) {
            return;
        }
        setVisibility(VISIBLE);
        TranslateAnimation anim = new TranslateAnimation(1, 0, 1, 0, 1, 1, 1, 0);
        anim.setDuration(200);//动画持续的时间为200ms
        anim.setFillEnabled(true);//使其可以填充效果从而不回到原地
        anim.setFillAfter(true);//不回到起始位置
        startAnimation(anim);
    }

    /**
     * 动画隐藏
     */
    public void dismiss() {
        if (!Config.previewSelectable || getVisibility() == GONE) {
            return;
        }
        TranslateAnimation anim = new TranslateAnimation(1, 0, 1, 0, 1, 0, 1, 1);
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

    /**
     * 更新选中图片的列表
     */
    public void notifyDataSetChanged() {
        if (mAdapter != null && Config.selectedPhotos != null) {
            mAdapter.setSelectedItem(mBinding.rv, Config.selectedPhotos.contains(Config.currentPath) ? Config.selectedPhotos.indexOf(Config.currentPath) : -1);
            mAdapter.notifyDataSetChanged();
            mBinding.rlSelected.setSelected(Config.selectedPhotos.contains(Config.currentPath));
            mBinding.tvSelected.setText(String.valueOf(Config.selectedPhotos.contains(Config.currentPath) ? Config.selectedPhotos.indexOf(Config.currentPath) + 1 : ""));
        }
    }
}
