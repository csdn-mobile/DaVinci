package net.csdn.davinci.ui.viewmodel;

import android.view.View;

import net.csdn.davinci.BusEvent;
import net.csdn.davinci.core.entity.DavinciMedia;
import net.csdn.davinci.core.entity.DavinciPhoto;
import net.csdn.davinci.core.entity.DavinciVideo;
import net.csdn.mvvm_java.bus.LiveDataBus;
import net.csdn.mvvm_java.viewmodel.BaseViewModel;

public class PreviewFragmentViewModel extends BaseViewModel {

    public boolean isVideo;
    public DavinciPhoto image;
    public DavinciVideo video;

    public PreviewFragmentViewModel() {
        super();
    }


    public boolean isLongerImage(int imageWidth, int imageHeight) {
        if (imageHeight == 0 || imageWidth == 0) {
            return false;
        }
        return imageHeight / imageWidth >= 3;
    }

    public View.OnLongClickListener getOnLongClickListener() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                LiveDataBus.getInstance().with(BusEvent.Preview.PREVIEW_LONG_CLICK, DavinciMedia.class).setValue(image);
                return false;
            }
        };
    }
}
