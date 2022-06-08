package net.csdn.davinci.mvvm.viewmodel;

import androidx.lifecycle.ViewModel;

/**
 * @author by KG on 2022/05/13
 */
public abstract class DavinciAdapterViewModel<T> extends ViewModel {

    public int mPosition;
    public T mData;

    protected DavinciAdapterViewModel(int position, T data) {
        super();
        this.mPosition = position;
        this.mData = data;
    }
}
