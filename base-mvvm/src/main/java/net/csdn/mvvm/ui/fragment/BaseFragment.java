package net.csdn.mvvm.ui.fragment;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    /**
     * 获取布局ID
     *
     * @return 布局ID
     */
    @LayoutRes
    public abstract int getLayoutId();
}
