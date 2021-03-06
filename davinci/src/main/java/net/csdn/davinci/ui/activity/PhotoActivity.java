package net.csdn.davinci.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import net.csdn.davinci.BR;
import net.csdn.davinci.BusEvent;
import net.csdn.davinci.Config;
import net.csdn.davinci.DaVinci;
import net.csdn.davinci.R;
import net.csdn.davinci.core.entity.Album;
import net.csdn.davinci.core.entity.Photo;
import net.csdn.davinci.core.photo.PhotoCaptureManager;
import net.csdn.davinci.databinding.ActivityPhotoBinding;
import net.csdn.davinci.ui.adapter.PhotoAdapter;
import net.csdn.davinci.ui.viewmodel.PhotoViewModel;
import net.csdn.davinci.utils.PermissionsUtils;
import net.csdn.mvvm_java.bus.LiveDataBus;
import net.csdn.mvvm_java.ui.activity.BaseBindingViewModelActivity;
import net.csdn.statusbar.StatusBar;
import net.csdn.statusbar.annotation.FontMode;

import java.io.IOException;
import java.util.ArrayList;

public class PhotoActivity extends BaseBindingViewModelActivity<ActivityPhotoBinding, PhotoViewModel> {

    private PhotoAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_photo;
    }

    @Override
    public int getVariableId() {
        return BR.viewmodel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusBar.Builder()
                .color(getResources().getColor(R.color.davinci_white))
                .fontMode(FontMode.DARK)
                .change(this);

        setBinding();
        setListener();
        registerBus();

        mViewModel.loadAlbum(this);
        changeConfirmStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mBinding.album.getVisibility() == View.VISIBLE) {
            mBinding.album.closeAlbum();
            mBinding.navigation.setArrowDown();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PhotoCaptureManager.REQUEST_TAKE_PHOTO: {
                PhotoCaptureManager.getInstance(getApplication()).galleryAddPic();
                Uri uri = PhotoCaptureManager.getInstance(getApplication()).getCurrentPhotoUri();
                Config.selectedPhotos.add(uri.toString());
                Config.previewPhotos.add(uri.toString());
                changeConfirmStatus();
                mAdapter.getDatas().add(0, new Photo(uri));
                mAdapter.notifyDataSetChanged();
            }
            break;
            case PreviewActivity.RESULT_PREVIEW: {
                finishAndSetResult();
            }
            break;
        }
    }

    private void setBinding() {
        // ????????????Adapter
        mAdapter = new PhotoAdapter(this, new PhotoAdapter.OnPhotoSelectChangeListener() {
            @Override
            public void onChange() {
                changeConfirmStatus();
            }
        }, new PhotoAdapter.OnCameraClickListener() {
            @Override
            public void onClick() {
                if (!PermissionsUtils.checkCameraPermission(PhotoActivity.this)) {
                    return;
                }
                openCamera();
            }
        });
        mBinding.setAdapter(mAdapter);
        // ???????????????????????????
        mViewModel.mAlbumList.observe(this, new Observer<ArrayList<Album>>() {
            @Override
            public void onChanged(ArrayList<Album> albums) {
                mBinding.album.setData(albums);
                selectAlbum(albums.get(0));
            }
        });
    }

    private void setListener() {
        mBinding.navigation.setListener(v -> onBackPressed(), v -> {
            if (mBinding.album.getVisibility() == View.GONE) {
                mBinding.album.openAlbum();
                mBinding.navigation.setArrowUp();
            } else {
                closeAlbum();
            }
        }, v -> finishAndSetResult());
    }

    private void registerBus() {
        // ????????????
        LiveDataBus.getInstance().with(BusEvent.Photo.ALBUM_SELECT, Album.class).observe(this, new Observer<Album>() {
            @Override
            public void onChanged(Album album) {
                selectAlbum(album);
                closeAlbum();
            }
        });
        // ??????????????????????????????
        LiveDataBus.getInstance().with(BusEvent.Photo.ALBUM_BLANK_CLICK).observe(this, new Observer<Object>() {
            @Override
            public void onChanged(Object o) {
                closeAlbum();
            }
        });
    }

    private void selectAlbum(Album album) {
        if (album == null) {
            return;
        }
        mBinding.navigation.setTitle(album.name);
        mAdapter.setDatas(album.photoList);

        ArrayList<String> list = new ArrayList<>();
        //uri
        for (Photo photo : album.photoList) {
            list.add(photo.uri.toString());
        }
        Config.previewPhotos = list;
    }

    private void closeAlbum() {
        mBinding.album.closeAlbum();
        mBinding.navigation.setArrowDown();
    }

    private void openCamera() {
        try {
            Intent intent = PhotoCaptureManager.getInstance(getApplication()).dispatchTakePictureIntent();
            startActivityForResult(intent, PhotoCaptureManager.REQUEST_TAKE_PHOTO);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ActivityNotFoundException e) {
            Log.e("PhotoPickerFragment", "No Activity Found to handle Intent", e);
        }
    }

    private void changeConfirmStatus() {
        if (Config.selectedPhotos.size() <= 0) {
            mBinding.navigation.setDoUnEnable();
        } else {
            mBinding.navigation.setDoEnable();
        }
    }

    private void finishAndSetResult() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(DaVinci.KEY_SELECTED_PHOTOS, Config.selectedPhotos);
        setResult(RESULT_OK, intent);
        finish();
    }
}
