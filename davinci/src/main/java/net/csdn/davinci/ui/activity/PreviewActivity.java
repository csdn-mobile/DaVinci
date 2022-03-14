package net.csdn.davinci.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.csdn.statusbar.StatusBar;
import com.csdn.statusbar.annotation.FontMode;

import net.csdn.davinci.Config;
import net.csdn.davinci.DaVinci;
import net.csdn.davinci.R;
import net.csdn.davinci.core.photo.PhotoHandleManager;
import net.csdn.davinci.core.photo.PhotoHandleManagerImpl;
import net.csdn.davinci.ui.adapter.PreviewPagerAdapter;
import net.csdn.davinci.ui.adapter.PreviewSelectedAdapter;
import net.csdn.davinci.ui.fragment.PreviewFragment;
import net.csdn.davinci.ui.view.PreviewBottomBar;
import net.csdn.davinci.ui.view.PreviewNavigation;
import net.csdn.davinci.utils.PermissionsUtils;

public class PreviewActivity extends AppCompatActivity {

    public static final int RESULT_PREVIEW = 2048;

    private ViewPager viewPager;
    private PreviewNavigation navigation;
    private PreviewBottomBar bottomBar;
    private TextView tvPage;

    private PhotoHandleManager mHandleManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        overridePendingTransition(R.anim.davinci_fade_in, R.anim.davinci_fade_out);

        StatusBar.Builder()
                .transparent(true)
                .fontMode(FontMode.LIGHT)
                .change(this);

        viewPager = findViewById(R.id.vp);
        navigation = findViewById(R.id.navigation);
        bottomBar = findViewById(R.id.bottom_bar);
        tvPage = findViewById(R.id.tv_page);

        tvPage.setVisibility(!Config.previewSelectable && Config.previewPhotos.size() > 1 ? View.VISIBLE : View.GONE);
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
        navigation.setOnBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        navigation.setOnConfirmClick(new PreviewNavigation.OnConfirmClickListener() {
            @Override
            public void onConfirmClick() {
                Intent intent = new Intent();
                intent.putStringArrayListExtra(DaVinci.KEY_SELECTED_PHOTOS, Config.selectedPhotos);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        bottomBar.setOnSelectedPhotoClickListener(new PreviewSelectedAdapter.OnSelectedPhotoClickListener() {
            @Override
            public void onClick(String path) {
                viewPager.setCurrentItem(Config.previewPhotos.indexOf(path), false);
            }
        });
    }

    private void setAdapter() {
        PreviewPagerAdapter adapter = new PreviewPagerAdapter(getSupportFragmentManager(), Config.previewPhotos, new PreviewFragment.OnPhotoClickListener() {
            @Override
            public void onSingleClick() {
                if (!Config.previewSelectable) {
                    finish();
                    return;
                }
                if (navigation.getVisibility() == View.VISIBLE) {
                    navigation.dismiss();
                    bottomBar.dismiss();
                } else {
                    navigation.show();
                    bottomBar.show();
                }
            }

            @Override
            public void onLongClick(String url) {
                if (!url.startsWith("http")) {
                    return;
                }
                mHandleManager.showLongClickDialog(url);
            }
        });
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Config.currentPath = Config.previewPhotos.get(position);
                bottomBar.notifyDataSetChanged();
                setPage();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setCurrentItem(Config.previewPhotos.indexOf(Config.currentPath), false);
    }

    private void setPage() {
        tvPage.setText(getResources().getString(R.string.davinci_pager_page, Config.previewPhotos.indexOf(Config.currentPath) + 1, Config.previewPhotos.size()));
    }

}
