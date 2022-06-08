package net.csdn.mvvm.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import net.csdn.mvvm.viewmodel.BaseViewModel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 使用DataBinding与ViewModel的Activity基类
 *
 * @author by KG on 2022/05/13
 */
public abstract class BaseBindingViewModelFragment<DB extends ViewDataBinding, VM extends BaseViewModel> extends BaseFragment {

    protected DB mBinding;
    protected VM mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        mViewModel = new ViewModelProvider(this).get(getViewModelClass());
        mBinding.setLifecycleOwner(this);
        mBinding.setVariable(getVariableId(), mViewModel);
        return mBinding.getRoot();
    }

    private Class<VM> getViewModelClass() {
        Type type = getClass().getGenericSuperclass();
        return (Class<VM>) ((ParameterizedType) type).getActualTypeArguments()[1];
    }

    /**
     * 获取ViewModel在DataBinding布局中的ID
     * 例：BR.viewModel
     *
     * @return variable的ID
     */
    public abstract int getVariableId();
}
