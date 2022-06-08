package net.csdn.davinci.ui.viewmodel;

import net.csdn.davinci.Config;
import net.csdn.davinci.mvvm.viewmodel.DavinciViewModel;

public class PreviewViewModel extends DavinciViewModel {
    public PreviewViewModel() {
        super();
    }

    public boolean isPageGone() {
        return !(!Config.previewSelectable && Config.previewPhotos.size() > 1);
    }
}
