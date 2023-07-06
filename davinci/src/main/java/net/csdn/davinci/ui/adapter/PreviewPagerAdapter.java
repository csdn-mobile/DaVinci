package net.csdn.davinci.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import net.csdn.davinci.ui.fragment.PreviewFragment;

import java.util.ArrayList;
import java.util.List;

public class PreviewPagerAdapter extends FragmentPagerAdapter {

    private List<Object> mDatas;

    public PreviewPagerAdapter(FragmentManager fm, List<Object> datas) {
        super(fm);
        this.mDatas = datas;
        if (this.mDatas == null) {
            this.mDatas = new ArrayList<>();
        }
    }

    @Override
    public Fragment getItem(int position) {
        return PreviewFragment.newInstance(mDatas.get(position));
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }
}
