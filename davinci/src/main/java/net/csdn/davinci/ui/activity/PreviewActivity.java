package net.csdn.davinci.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.csdn.statusbar.StatusBar;
import com.csdn.statusbar.annotation.FontMode;

import net.csdn.davinci.Config;
import net.csdn.davinci.R;
import net.csdn.davinci.ui.adapter.PreviewPagerAdapter;
import net.csdn.davinci.ui.adapter.PreviewSelectedAdapter;
import net.csdn.davinci.ui.view.PreviewBottomBar;
import net.csdn.davinci.ui.view.PreviewNavigation;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class PreviewActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PreviewNavigation navigation;
    private PreviewBottomBar bottomBar;
    private TextView tvPage;

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

        tvPage.setVisibility(!Config.previewSelectable && Config.showPhotos.size() > 1 ? View.VISIBLE : View.GONE);

        setPage();
        setListener();
        setAdapter();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.davinci_fade_in, R.anim.davinci_fade_out);
    }

    private void setListener() {
        navigation.setOnBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        navigation.setOnConfirmClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bottomBar.setOnSelectedPhotoClickListener(new PreviewSelectedAdapter.OnSelectedPhotoClickListener() {
            @Override
            public void onClick(String path) {
                viewPager.setCurrentItem(Config.showPhotos.indexOf(path), false);
            }
        });
    }

    private void setAdapter() {
        PreviewPagerAdapter adapter = new PreviewPagerAdapter(getSupportFragmentManager(), Config.showPhotos, new ImageViewTouch.OnImageViewTouchSingleTapListener() {
            @Override
            public void onSingleTapConfirmed() {
                if (!Config.previewSelectable) {
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
        });
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Config.currentPath = Config.showPhotos.get(position);
                bottomBar.notifyDataSetChanged();
                setPage();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setCurrentItem(Config.showPhotos.indexOf(Config.currentPath), false);
    }

    private void setPage() {
        tvPage.setText(getResources().getString(R.string.davinci_pager_page, Config.showPhotos.indexOf(Config.currentPath) + 1, Config.showPhotos.size()));
    }
}
