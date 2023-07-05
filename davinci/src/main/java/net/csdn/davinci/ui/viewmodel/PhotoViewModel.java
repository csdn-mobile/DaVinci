package net.csdn.davinci.ui.viewmodel;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import net.csdn.davinci.Config;
import net.csdn.davinci.DaVinci;
import net.csdn.davinci.core.album.AlbumHelper;
import net.csdn.davinci.core.album.AlbumResultCallback;
import net.csdn.davinci.core.entity.Album;
import net.csdn.mvvm_java.viewmodel.BaseViewModel;

import java.util.ArrayList;

public class PhotoViewModel extends BaseViewModel {

    public final int typeImage = 1;
    public final int typeVideo = 2;

    public int typeVisibility;
    /**
     * 相册数据源
     */
    private AlbumHelper mAlbumHelper;
    /**
     * 相簿列表
     */
    public MutableLiveData<ArrayList<Album>> mAlbumList;
    public MutableLiveData<Integer> mSelectType;

    public PhotoViewModel() {
        super();
        typeVisibility = Config.selectType == DaVinci.SELECT_IMAGE_VIDEO ? View.VISIBLE : View.GONE;
        mAlbumList = new MutableLiveData<>();
        mSelectType = new MutableLiveData<>(typeImage);
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

    public void onSelectType(int type) {
        if (mSelectType.getValue() == null || mSelectType.getValue() == type) {
            return;
        }
        mSelectType.setValue(type);
    }
}
