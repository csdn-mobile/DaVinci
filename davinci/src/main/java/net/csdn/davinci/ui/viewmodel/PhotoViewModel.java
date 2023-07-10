package net.csdn.davinci.ui.viewmodel;

import android.view.View;

import androidx.lifecycle.MutableLiveData;

import net.csdn.davinci.Config;
import net.csdn.davinci.DaVinci;
import net.csdn.davinci.core.entity.Album;
import net.csdn.mvvm_java.viewmodel.BaseViewModel;

import java.util.ArrayList;

public class PhotoViewModel extends BaseViewModel {

    public int typeVisibility;
    public int selectType;
    public String selectLimitDesc;

    public MutableLiveData<ArrayList<Album>> albumList;

    public MutableLiveData<Integer> selectImageVisibility;
    public MutableLiveData<Integer> permissionVisibility;

    public PhotoViewModel() {
        super();
        typeVisibility = Config.selectType == DaVinci.SelectType.IMAGE_VIDEO ? View.VISIBLE : View.GONE;
        albumList = new MutableLiveData<>();
        if (Config.selectType == DaVinci.SelectType.IMAGE_VIDEO) {
            selectLimitDesc = "最多可添加" + Config.maxSelectable + "张照片或1个视频";
        } else if (Config.selectType == DaVinci.SelectType.VIDEO) {
            selectLimitDesc = "最多可添加1个视频";
        } else {
            selectLimitDesc = "最多可添加" + Config.maxSelectable + "张照片";
        }
        selectImageVisibility = new MutableLiveData<>(Config.selectedPhotos.size() > 0 ? View.VISIBLE : View.GONE);
        permissionVisibility = new MutableLiveData<>(View.VISIBLE);
    }
}
