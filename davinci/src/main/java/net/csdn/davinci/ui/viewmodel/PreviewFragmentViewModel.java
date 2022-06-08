package net.csdn.davinci.ui.viewmodel;

import android.view.View;

import net.csdn.davinci.BusEvent;
import net.csdn.davinci.mvvm.bus.DavinciBus;
import net.csdn.davinci.mvvm.viewmodel.DavinciViewModel;

public class PreviewFragmentViewModel extends DavinciViewModel {

    private String mPath;

    public PreviewFragmentViewModel() {
        super();
    }

    public void setPath(String path) {
        this.mPath = path;
    }

    public String getPath() {
        return mPath;
    }

    public boolean isLongerImage(int imageWidth, int imageHeight) {
        if (imageHeight == 0 || imageWidth == 0) {
            return false;
        }
        return imageHeight / imageWidth >= 3;
    }


    public View.OnClickListener getOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DavinciBus.getInstance().with(BusEvent.Preview.PREVIEW_CLICK).setValue(null);
            }
        };
    }

    public View.OnLongClickListener getOnLongClickListener() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DavinciBus.getInstance().with(BusEvent.Preview.PREVIEW_LONG_CLICK, String.class).setValue(mPath);
                return false;
            }
        };
    }
}
