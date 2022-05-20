package net.csdn.mvvm.ui.activity;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author by KG on 2022/05/13
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 获取布局ID
     *
     * @return 布局ID
     */
    @LayoutRes
    public abstract int getLayoutId();

}
