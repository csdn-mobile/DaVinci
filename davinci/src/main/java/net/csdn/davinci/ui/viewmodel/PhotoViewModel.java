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
    public String selectLimitDesc;
    private AlbumHelper mAlbumHelper;

    public MutableLiveData<ArrayList<Album>> albumList;
    public MutableLiveData<Integer> selectType;

    public MutableLiveData<Integer> selectImageVisibility;

    public PhotoViewModel() {
        super();
        typeVisibility = Config.selectType == DaVinci.SELECT_IMAGE_VIDEO ? View.VISIBLE : View.GONE;
        albumList = new MutableLiveData<>();
        selectType = new MutableLiveData<>(typeImage);
        if (Config.selectType == DaVinci.SELECT_IMAGE_VIDEO) {
            selectLimitDesc = "最多可添加" + Config.maxSelectable + "张照片或1个视频";
        } else if (Config.selectType == DaVinci.SELECT_VIDEO) {
            selectLimitDesc = "最多可添加1个视频";
        } else {
            selectLimitDesc = "最多可添加" + Config.maxSelectable + "张照片";
        }
        selectImageVisibility = new MutableLiveData<>(Config.selectedPhotos.size() > 0 ? View.VISIBLE : View.GONE);
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
                albumList.setValue(albums);
            }
        });
    }

    public void onDestroy() {
        if (mAlbumHelper != null) {
            mAlbumHelper.onDestroy();
        }
    }

    public void onSelectType(int type) {
        if (selectType.getValue() == null || selectType.getValue() == type) {
            return;
        }
        selectType.setValue(type);
    }
}
