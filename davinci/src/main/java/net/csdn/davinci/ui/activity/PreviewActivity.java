package net.csdn.davinci.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.csdn.statusbar.StatusBar;
import com.csdn.statusbar.annotation.FontMode;

import net.csdn.davinci.BR;
import net.csdn.davinci.BusEvent;
import net.csdn.davinci.Config;
import net.csdn.davinci.DaVinci;
import net.csdn.davinci.R;
import net.csdn.davinci.core.photo.PhotoHandleManager;
import net.csdn.davinci.core.photo.PhotoHandleManagerImpl;
import net.csdn.davinci.databinding.ActivityPreviewBinding;
import net.csdn.davinci.ui.adapter.PreviewPagerAdapter;
import net.csdn.davinci.ui.fragment.PreviewFragment;
import net.csdn.davinci.ui.viewmodel.PreviewViewModel;
import net.csdn.davinci.utils.PermissionsUtils;
import net.csdn.mvvm.bus.LiveDataBus;
import net.csdn.mvvm.ui.activity.BaseBindingViewModelActivity;

public class PreviewActivity extends BaseBindingViewModelActivity<ActivityPreviewBinding, PreviewViewModel> {

    public static final int RESULT_PREVIEW = 2048;

    private PhotoHandleManager mHandleManager;

    @Override
    public int getVariableId() {
        return BR.viewmodel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_preview;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.davinci_fade_in, R.anim.davinci_fade_out);

        StatusBar.Builder()
                .transparent(true)
                .fontMode(FontMode.LIGHT)
                .change(this);

        if (Config.previewPhotos == null) {
            finish();
            return;
        }
        mHandleManager = new PhotoHandleManagerImpl(this);

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
                    mHandleManager.download(null);
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.davinci_no_permission_write), Toast.LENGTH_LONG).show();
            }
        }
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

    private void setListener() {
        mBinding.navigation.setListener(v -> finish(), v -> {
            Intent intent = new Intent();
            intent.putStringArrayListExtra(DaVinci.KEY_SELECTED_PHOTOS, Config.selectedPhotos);
            setResult(RESULT_OK, intent);
            finish();
        });

        LiveDataBus.getInstance().with(BusEvent.Preview.PREVIEW_SELECTED_CLICK, String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String path) {
                mBinding.viewPager.setCurrentItem(Config.previewPhotos.indexOf(path), false);
            }
        });

        LiveDataBus.getInstance().with(BusEvent.Preview.PREVIEW_CLICK).observe(this, new Observer<Object>() {
            @Override
            public void onChanged(Object o) {
                if (!Config.previewSelectable) {
                    finish();
                    return;
                }
                if (mBinding.navigation.getVisibility() == View.VISIBLE) {
                    mBinding.navigation.dismiss();
                    mBinding.bottomBar.dismiss();
                } else {
                    mBinding.navigation.show();
                    mBinding.bottomBar.show();
                }
            }
        });

        LiveDataBus.getInstance().with(BusEvent.Preview.PREVIEW_LONG_CLICK, String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String url) {
                if (!url.startsWith("http")) {
                    return;
                }
                mHandleManager.showLongClickDialog(url);
            }
        });
    }

    private void setAdapter() {
        PreviewPagerAdapter adapter = new PreviewPagerAdapter(getSupportFragmentManager(), Config.previewPhotos);
        mBinding.viewPager.setAdapter(adapter);
        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Config.currentPath = Config.previewPhotos.get(position);
                mBinding.bottomBar.notifyDataSetChanged();
                setPage();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBinding.viewPager.setCurrentItem(Config.previewPhotos.indexOf(Config.currentPath), false);
    }

    private void setPage() {
        if (Config.previewPhotos == null) {
            return;
        }
        mBinding.tvPage.setText(getResources().getString(R.string.davinci_pager_page, Config.previewPhotos.indexOf(Config.currentPath) + 1, Config.previewPhotos.size()));
    }

}
