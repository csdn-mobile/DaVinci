package net.csdn.davinci.ui.viewmodel;

import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import net.csdn.davinci.Config;
import net.csdn.davinci.DaVinci;
import net.csdn.davinci.R;
import net.csdn.davinci.core.entity.Album;
import net.csdn.mvvm_java.viewmodel.BaseViewModel;

import java.util.ArrayList;

public class PhotoViewModel extends BaseViewModel {

    public int typeVisibility;
    public int selectType;
    public String selectLimitDesc;

    public MutableLiveData<ArrayList<Album>> albumList;

    public MutableLiveData<Integer> selectImageVisibility;

    public PhotoViewModel() {
        super();
        typeVisibility = Config.selectType == DaVinci.SELECT_IMAGE_VIDEO ? View.VISIBLE : View.GONE;
        albumList = new MutableLiveData<>();
        if (Config.selectType == DaVinci.SELECT_IMAGE_VIDEO) {
            selectLimitDesc = "最多可添加" + Config.maxSelectable + "张照片或1个视频";
        } else if (Config.selectType == DaVinci.SELECT_VIDEO) {
            selectLimitDesc = "最多可添加1个视频";
        } else {
            selectLimitDesc = "最多可添加" + Config.maxSelectable + "张照片";
        }
        selectImageVisibility = new MutableLiveData<>(Config.selectedPhotos.size() > 0 ? View.VISIBLE : View.GONE);
    }
}
