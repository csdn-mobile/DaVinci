package net.csdn.davinci.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import net.csdn.davinci.ui.fragment.PreviewFragment;

import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class PreviewPagerAdapter extends FragmentPagerAdapter {

    private List<String> mDatas;
    private ImageViewTouch.OnImageViewTouchSingleTapListener mListener;

    public PreviewPagerAdapter(FragmentManager fm, List<String> datas, ImageViewTouch.OnImageViewTouchSingleTapListener listener) {
        super(fm);
        this.mDatas = datas;
        if (this.mDatas == null) {
            this.mDatas = new ArrayList<>();
        }
        this.mListener = listener;
    }

    @Override
    public Fragment getItem(int position) {
        PreviewFragment fragment = PreviewFragment.newInstance(mDatas.get(position));
        fragment.setOnImageViewTouchSingleTapListener(mListener);
        return fragment;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }
}
