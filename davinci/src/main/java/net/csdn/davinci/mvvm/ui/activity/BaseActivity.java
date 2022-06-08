package net.csdn.davinci.mvvm.ui.activity;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import net.csdn.davinci.mvvm.DavinciContext;

/**
 * @author by KG on 2022/05/13
 */
abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DavinciContext.application = getApplication();
    }

    /**
     * 获取布局ID
     *
     * @return 布局ID
     */
    @LayoutRes
    public abstract int getLayoutId();

}
