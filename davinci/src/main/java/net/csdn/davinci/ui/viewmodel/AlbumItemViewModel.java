package net.csdn.davinci.ui.viewmodel;

import android.view.View;

import net.csdn.davinci.BusEvent;
import net.csdn.davinci.core.entity.Album;
import net.csdn.davinci.utils.DensityUtils;
import net.csdn.mvvm_java.AppContext;
import net.csdn.mvvm_java.bus.LiveDataBus;
import net.csdn.mvvm_java.viewmodel.BaseAdapterViewModel;

public class AlbumItemViewModel extends BaseAdapterViewModel<Album> {

    public AlbumItemViewModel(int position, Album data) {
        super(position, data);
    }

    public int getPhotoCount() {
        int count = 0;
        if (mData.photoList != null) {
            count += mData.photoList.size();
        }
        if (mData.videoList != null) {
            count += mData.videoList.size();
        }
        return count;
    }

    public int getImageResize() {
        return DensityUtils.dp2px(AppContext.application, 40);
    }

    public View.OnClickListener onClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveDataBus.getInstance().with(BusEvent.Photo.ALBUM_SELECT, Album.class).setValue(mData);
            }
        };
    }
}
