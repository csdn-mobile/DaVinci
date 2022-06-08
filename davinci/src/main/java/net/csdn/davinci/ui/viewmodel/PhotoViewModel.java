package net.csdn.davinci.ui.viewmodel;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import net.csdn.davinci.core.album.AlbumHelper;
import net.csdn.davinci.core.album.AlbumResultCallback;
import net.csdn.davinci.core.entity.Album;
import net.csdn.davinci.mvvm.viewmodel.DavinciViewModel;

import java.util.ArrayList;

public class PhotoViewModel extends DavinciViewModel {

    /**
     * 相册数据源
     */
    private AlbumHelper mAlbumHelper;
    /**
     * 相簿列表
     */
    public MutableLiveData<ArrayList<Album>> mAlbumList;

    public PhotoViewModel() {
        super();
        mAlbumList = new MutableLiveData<>();
    }

    public void loadAlbum(Activity activity) {
        if (mAlbumHelper == null) {
            mAlbumHelper = new AlbumHelper();
        }
        mAlbumHelper.onCreate(activity);
        mAlbumHelper.loadAlbums(new AlbumResultCallback() {
            @Override
            public void onResult(ArrayList<Album> albums) {
                Log.e("AlbumLoad", "onLoadFinished====" + albums.toString());
                mAlbumList.setValue(albums);
            }
        });
    }

    public void onDestroy() {
        if (mAlbumHelper != null) {
            mAlbumHelper.onDestroy();
        }
    }
}
