package net.csdn.davinci.ui.viewmodel;

import net.csdn.davinci.utils.DensityUtils;
import net.csdn.mvvm.AppContext;
import net.csdn.mvvm.viewmodel.BaseAdapterViewModel;

public class PreviewSelectedItemViewModel extends BaseAdapterViewModel<String> {

    public int mSelectPostion;

    public PreviewSelectedItemViewModel(int selectPostion, int position, String data) {
        super(position, data);
        mSelectPostion = selectPostion;
    }

    public int getMarginStart() {
        return mPosition == 0 ? DensityUtils.dp2px(AppContext.application, 14) : 0;
    }

    public int getImageResize() {
        return DensityUtils.dp2px(AppContext.application, 84);
    }

}
