package net.csdn.davinci.ui.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import net.csdn.davinci.BR;
import net.csdn.davinci.BusEvent;
import net.csdn.davinci.Config;
import net.csdn.davinci.R;
import net.csdn.davinci.core.entity.DavinciMedia;
import net.csdn.davinci.core.entity.DavinciPhoto;
import net.csdn.davinci.core.entity.DavinciVideo;
import net.csdn.davinci.core.photo.PhotoHandleManager;
import net.csdn.davinci.core.photo.PhotoHandleManagerImpl;
import net.csdn.davinci.databinding.DavinciActivityPreviewBinding;
import net.csdn.davinci.ui.adapter.PreviewPagerAdapter;
import net.csdn.davinci.ui.viewmodel.PreviewViewModel;
import net.csdn.davinci.utils.DavinciToastUtils;
import net.csdn.davinci.utils.PermissionsUtils;
import net.csdn.davinci.utils.SharedPreferenceUtil;
import net.csdn.mvvm_java.bus.LiveDataBus;
import net.csdn.mvvm_java.ui.activity.BaseBindingViewModelActivity;
import net.csdn.statusbar.StatusBar;
import net.csdn.statusbar.annotation.FontMode;

public class PreviewActivity extends BaseBindingViewModelActivity<DavinciActivityPreviewBinding, PreviewViewModel> {

    private PhotoHandleManager mHandleManager;

    @Override
    public int getVariableId() {
        return BR.viewmodel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.davinci_activity_preview;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(Config.isDayStyle ? R.style.DavinciTheme_Day : R.style.DavinciTheme_Night);
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.davinci_fade_in, R.anim.davinci_fade_out);
        initStatusBar();

        initialize();
        setPage();
        setListener();
        setAdapter();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.davinci_fade_in, R.anim.davinci_fade_out);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void initialize() {
        if (Config.previewMedias == null) {
            finish();
            return;
        }
        mHandleManager = new PhotoHandleManagerImpl(this);
    }

    private void initStatusBar() {
        StatusBar.Builder()
                .transparent(true)
                .fontMode(FontMode.LIGHT)
                .change(this);
        ViewGroup.LayoutParams layoutParams = mBinding.viewStatusBar.getLayoutParams();
        layoutParams.height = StatusBar.getHeight(this);
    }

    private void setListener() {
        LiveDataBus.getInstance().with(BusEvent.Preview.PREVIEW_CLICK).observe(this, new Observer<Object>() {
            @Override
            public void onChanged(Object o) {
                if (!Config.previewSelectable) {
                    finish();
                }
            }
        });
        LiveDataBus.getInstance().with(BusEvent.Preview.PREVIEW_LONG_CLICK, DavinciMedia.class).observe(this, new Observer<DavinciMedia>() {
            @Override
            public void onChanged(DavinciMedia media) {
                if (media == null || !media.path.startsWith("http")) {
                    return;
                }
                mHandleManager.showLongClickDialog(media.path);
            }
        });
        mBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.llSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean canSelected = false;
                boolean isVideo = false;
                if (Config.currentMedia instanceof DavinciVideo) {
                    if (!mBinding.tvSelected.isSelected() && Config.selectedVideos.size() >= 1) {
                        DavinciToastUtils.showToast(PreviewActivity.this, getResources().getString(R.string.davinci_over_max_count_tips_video, 1));
                        return;
                    }
                    canSelected = true;
                    isVideo = true;
                } else if (Config.currentMedia instanceof DavinciPhoto) {
                    if (!mBinding.tvSelected.isSelected() && Config.selectedPhotos.size() >= Config.maxSelectable) {
                        DavinciToastUtils.showToast(PreviewActivity.this, getResources().getString(R.string.davinci_over_max_count_tips, Config.maxSelectable));
                        return;
                    }
                    canSelected = true;
                }
                if (!canSelected) {
                    return;
                }
                try {
                    if (view.isSelected()) {
                        // 取消选中
                        if (isVideo) {
                            Config.selectedVideos.remove((DavinciVideo) Config.currentMedia);
                        } else {
                            Config.selectedPhotos.remove((DavinciPhoto) Config.currentMedia);
                        }
                        mBinding.tvSelected.setText("");
                        view.setSelected(false);
                    } else {
                        // 选中图片
                        if (isVideo) {
                            Config.selectedVideos.add((DavinciVideo) Config.currentMedia);
                            mBinding.tvSelected.setText(String.valueOf(Config.selectedVideos.indexOf((DavinciVideo) Config.currentMedia) + 1));
                        } else {
                            Config.selectedPhotos.add((DavinciPhoto) Config.currentMedia);
                            mBinding.tvSelected.setText(String.valueOf(Config.selectedPhotos.indexOf((DavinciPhoto) Config.currentMedia) + 1));
                        }
                        view.setSelected(true);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setAdapter() {
        PreviewPagerAdapter adapter = new PreviewPagerAdapter(getSupportFragmentManager(), Config.previewMedias);
        mBinding.viewPager.setAdapter(adapter);
        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Object object = Config.previewMedias.get(position);
                if (object instanceof DavinciMedia) {
                    Config.currentMedia = (DavinciMedia) object;
                    setSelectStatus();
                    setPage();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (Config.previewMedias != null && Config.currentMedia != null) {
            mBinding.viewPager.setCurrentItem(Config.previewMedias.indexOf(Config.currentMedia), false);
        }
        setSelectStatus();
    }

    private void setSelectStatus() {
        boolean isSelected = false;
        if (Config.currentMedia instanceof DavinciVideo) {
            if (Config.selectedVideos.contains((DavinciVideo) Config.currentMedia)) {
                isSelected = true;
                mBinding.tvSelected.setText(String.valueOf(Config.selectedVideos.indexOf((DavinciVideo) Config.currentMedia) + 1));
            }
        } else if (Config.currentMedia instanceof DavinciPhoto) {
            if (Config.selectedPhotos.contains((DavinciPhoto) Config.currentMedia)) {
                isSelected = true;
                mBinding.tvSelected.setText(String.valueOf(Config.selectedPhotos.indexOf((DavinciPhoto) Config.currentMedia) + 1));
            }
        }
        mBinding.llSelected.setSelected(isSelected);
        if (!isSelected) {
            mBinding.tvSelected.setText("");
        }
    }

    private void setPage() {
        if (Config.previewMedias == null) {
            return;
        }
        mBinding.tvPage.setText(getResources().getString(R.string.davinci_pager_page, Config.previewMedias.indexOf(Config.currentMedia) + 1, Config.previewMedias.size()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionsUtils.REQUEST_EXTERNAL_WRITE) {
            // 判断是否所有的权限都已经授予了
            boolean isAllGranted = true;
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }
            if (isAllGranted) {
                if (mHandleManager != null) {
                    // 如果url为null，则使用之前设置的url继续下载
                    SharedPreferenceUtil.putSharedPreferencesBoolean(PreviewActivity.this, SharedPreferenceUtil.STATUS_PERMISSION_GRANTED, true);
                    mHandleManager.download(null);
                }
            } else {
                DavinciToastUtils.showToast(this, getResources().getString(R.string.davinci_no_permission_write));
            }
        }
    }

}
