package net.csdn.mvvm.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * 使用DataBinding的Activity基类
 *
 * @author by KG on 2022/05/13
 */
public abstract class BaseBindingActivity<DB extends ViewDataBinding> extends BaseActivity {

    protected DB mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        mBinding.setLifecycleOwner(this);
    }
}
