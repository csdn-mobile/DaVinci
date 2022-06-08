package net.csdn.davinci.mvvm.ui.adapter;

import androidx.annotation.LayoutRes;
import androidx.databinding.ViewDataBinding;

import net.csdn.davinci.mvvm.viewmodel.DavinciAdapterViewModel;

import java.util.List;

/**
 * 单个布局使用DataBinding和ViewModel的Adapter
 * <p>
 * （注：此处的ViewModel仅是将逻辑挪移到ViewModel中，没有生命周期作用。
 * 因为每个ViewModel只会在Owner中保存一份，并且数据的恢复应该由整个页面的ViewModel负责）
 *
 * @author by KG on 2022/05/19
 */
public class DavinciOriginAdapter<T, DB extends ViewDataBinding> extends BaseAdapter<T, DB> {

    private final Class<? extends DavinciAdapterViewModel<T>> mVmClass;

    public DavinciOriginAdapter(@LayoutRes int layoutId, int variableId, Class<? extends DavinciAdapterViewModel<T>> clazz) {
        this(layoutId, variableId, clazz, null);
    }

    public DavinciOriginAdapter(@LayoutRes int layoutId, int variableId, Class<? extends DavinciAdapterViewModel<T>> clazz, List<T> datas) {
        super(layoutId, variableId, datas);
        this.mVmClass = clazz;
    }

    @Override
    public void onBind(int position, DB dataBinding, T data) {
        DavinciAdapterViewModel<T> instance = getViewModelInstance(position, data);
        if (instance != null) {
            dataBinding.setVariable(mVariableId, instance);
        }
    }

    private DavinciAdapterViewModel<T> getViewModelInstance(int position, T data) {
        DavinciAdapterViewModel<T> instance = null;
        if (mVmClass != null) {
            try {
                instance = mVmClass.getDeclaredConstructor(int.class, data.getClass()).newInstance(position, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
}
