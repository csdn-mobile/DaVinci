package net.csdn.davinci.ui.viewmodel;

import net.csdn.davinci.utils.DensityUtils;
import net.csdn.davinci.mvvm.DavinciContext;
import net.csdn.davinci.mvvm.viewmodel.DavinciAdapterViewModel;

public class PreviewSelectedItemViewModel extends DavinciAdapterViewModel<String> {

    public int mSelectPostion;

    public PreviewSelectedItemViewModel(int selectPostion, int position, String data) {
        super(position, data);
        mSelectPostion = selectPostion;
    }

    public int getMarginStart() {
        return mPosition == 0 ? DensityUtils.dp2px(DavinciContext.application, 14) : 0;
    }

    public int getImageResize() {
        return DensityUtils.dp2px(DavinciContext.application, 84);
    }

}
