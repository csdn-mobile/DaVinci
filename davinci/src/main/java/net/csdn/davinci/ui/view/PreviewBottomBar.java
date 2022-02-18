package net.csdn.davinci.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.csdn.davinci.Config;
import net.csdn.davinci.R;
import net.csdn.davinci.ui.adapter.PreviewSelectedAdapter;

public class PreviewBottomBar extends LinearLayout {

    private RelativeLayout rlSelected;
    private TextView tvSelected;
    private RecyclerView rv;

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
        View view = LayoutInflater.from(context).inflate(R.layout.view_preview_bottom_bar, this);

        rlSelected = view.findViewById(R.id.rl_selected);
        tvSelected = view.findViewById(R.id.tv_selected);
        rv = view.findViewById(R.id.rv);

        setVisibility(Config.previewSelectable ? VISIBLE : GONE);
        setListener();
        setAdapter();
    }

    private void setListener() {
        rlSelected.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tvSelected.isSelected() && Config.selectedPhotos.size() >= Config.maxSelectable) {
                    Toast.makeText(getContext(), getContext().getString(R.string.davinci_over_max_count_tips, Config.maxSelectable), Toast.LENGTH_SHORT).show();
                    return;
                }
                view.setSelected(!view.isSelected());
                if (view.isSelected()) {
                    // 选中图片
                    Config.selectedPhotos.add(Config.currentPath);
                    tvSelected.setText(String.valueOf(Config.selectedPhotos.indexOf(Config.currentPath) + 1));
                } else {
                    // 取消选中
                    Config.selectedPhotos.remove(Config.currentPath);
                    tvSelected.setText("");
                }
                if (mAdapter != null) {
                    mAdapter.setSelectedItem(rv, Config.selectedPhotos.indexOf(Config.currentPath));
                    mAdapter.setDatas(Config.selectedPhotos);
                }
            }
        });
    }

    private void setAdapter() {
        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new PreviewSelectedAdapter(Config.selectedPhotos);
        rv.setAdapter(mAdapter);

        notifyDataSetChanged();
    }

    /**
     * 设置选中的图片点击事件
     */
    public void setOnSelectedPhotoClickListener(PreviewSelectedAdapter.OnSelectedPhotoClickListener listener) {
        if (mAdapter != null) {
            mAdapter.setOnSelectedPhotoClickListener(listener);
        }
    }

    /**
     * 动画显示
     */
    public void show() {
        if (rlSelected == null || !Config.previewSelectable || getVisibility() == VISIBLE) {
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
        if (rlSelected == null || !Config.previewSelectable || getVisibility() == GONE) {
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
        if (mAdapter != null) {
            mAdapter.setSelectedItem(rv, Config.selectedPhotos.contains(Config.currentPath) ? Config.selectedPhotos.indexOf(Config.currentPath) : -1);
            mAdapter.notifyDataSetChanged();
            rlSelected.setSelected(Config.selectedPhotos.contains(Config.currentPath));
            tvSelected.setText(String.valueOf(Config.selectedPhotos.contains(Config.currentPath) ? Config.selectedPhotos.indexOf(Config.currentPath) + 1 : ""));
        }
    }
}
