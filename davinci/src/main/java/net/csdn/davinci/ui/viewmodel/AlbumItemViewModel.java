package net.csdn.davinci.ui.viewmodel;

import android.view.View;

import net.csdn.davinci.BusEvent;
import net.csdn.davinci.core.entity.Album;
import net.csdn.davinci.utils.DensityUtils;
import net.csdn.davinci.mvvm.DavinciContext;
import net.csdn.davinci.mvvm.bus.DavinciBus;
import net.csdn.davinci.mvvm.viewmodel.DavinciAdapterViewModel;

public class AlbumItemViewModel extends DavinciAdapterViewModel<Album> {

    protected AlbumItemViewModel(int position, Album data) {
        super(position, data);
    }

    public int getPhotoCount() {
        return mData.photoList == null ? 0 : mData.photoList.size();
    }

    public int getImageResize() {
        return DensityUtils.dp2px(DavinciContext.application, 40);
    }

    public View.OnClickListener onClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DavinciBus.getInstance().with(BusEvent.Photo.ALBUM_SELECT, Album.class).setValue(mData);
            }
        };
    }
}
