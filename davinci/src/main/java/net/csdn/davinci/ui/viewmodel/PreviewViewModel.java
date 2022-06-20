package net.csdn.davinci.ui.viewmodel;

import net.csdn.davinci.Config;
import net.csdn.mvvm_java.viewmodel.BaseViewModel;

public class PreviewViewModel extends BaseViewModel {
    public PreviewViewModel() {
        super();
    }

    public boolean isPageGone() {
        return !(!Config.previewSelectable && Config.previewPhotos.size() > 1);
    }
}
