package net.csdn.davinci.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;

import net.csdn.davinci.Config;
import net.csdn.mvvm.viewmodel.BaseViewModel;

public class PhotoViewModel extends BaseViewModel {

    // 图片列数
    public MutableLiveData<Integer> mColumn;

    public PhotoViewModel() {
        super();
        mColumn = new MutableLiveData<>(Config.column);
    }
}
