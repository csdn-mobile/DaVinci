package net.csdn.davinci.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.github.chrisbanes.photoview.PhotoView;

public class PreviewViewPager extends ViewPager {

    public PreviewViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof PhotoView) {
            if (((PhotoView) v).getScale() != 1) {
                return true;
            }
            return super.canScroll(v, checkV, dx, x, y);
        }
        return super.canScroll(v, checkV, dx, x, y);
    }
}
