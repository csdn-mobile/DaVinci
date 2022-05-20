package net.csdn.mvvm.viewmodel;

import androidx.lifecycle.ViewModel;

/**
 * @author by KG on 2022/05/13
 */
public abstract class BaseAdapterViewModel<T> extends ViewModel {

    public T mData;

    protected BaseAdapterViewModel(T data) {
        super();
        this.mData = data;
    }
}
