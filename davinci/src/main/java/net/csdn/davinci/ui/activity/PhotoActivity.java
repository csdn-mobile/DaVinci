package net.csdn.davinci.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import net.csdn.davinci.BR;
import net.csdn.davinci.BusEvent;
import net.csdn.davinci.Config;
import net.csdn.davinci.DaVinci;
import net.csdn.davinci.R;
import net.csdn.davinci.core.album.AlbumHelper;
import net.csdn.davinci.core.entity.Album;
import net.csdn.davinci.core.entity.DavinciPhoto;
import net.csdn.davinci.core.entity.DavinciVideo;
import net.csdn.davinci.core.photo.PhotoCaptureManager;
import net.csdn.davinci.databinding.ActivityPhotoBinding;
import net.csdn.davinci.ui.adapter.MediaPreviewAdapter;
import net.csdn.davinci.ui.adapter.PhotoAdapter;
import net.csdn.davinci.ui.adapter.VideoAdapter;
import net.csdn.davinci.ui.viewmodel.PhotoViewModel;
import net.csdn.davinci.utils.DavinciToastUtils;
import net.csdn.davinci.utils.PermissionsUtils;
import net.csdn.davinci.utils.ResourceUtils;
import net.csdn.mvvm_java.bus.LiveDataBus;
import net.csdn.mvvm_java.ui.activity.BaseBindingViewModelActivity;
import net.csdn.statusbar.StatusBar;
import net.csdn.statusbar.annotation.FontMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhotoActivity extends BaseBindingViewModelActivity<ActivityPhotoBinding, PhotoViewModel> {

    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_VIDEO = 2;

    private PhotoAdapter mPhotoAdapter;
    private VideoAdapter mVideoAdapter;
    private MediaPreviewAdapter mPreviewAdapter;

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
        setTheme(Config.isDayStyle ? R.style.DavinciTheme_Day : R.style.DavinciTheme_Night);
        super.onCreate(savedInstanceState);
        StatusBar.Builder()
                .color(ResourceUtils.getColorFromAttr(this, R.attr.davinciBackgroundColor))
                .fontMode(Config.isDayStyle ? FontMode.DARK : FontMode.LIGHT)
                .change(this);

        setBinding();
        setListener();
        registerBus();

        loadAlbumWithPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mViewModel.selectType == TYPE_IMAGE && mPhotoAdapter != null) {
            mPhotoAdapter.notifyDataSetChanged();
        } else if (mViewModel.selectType == TYPE_VIDEO && mVideoAdapter != null) {
            mVideoAdapter.notifyDataSetChanged();
        }
        changeConfirmStatus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        if (requestCode == PhotoCaptureManager.REQUEST_TAKE_PHOTO) {
            PhotoCaptureManager.getInstance(getApplication()).galleryAddPic();
            Uri uri = PhotoCaptureManager.getInstance(getApplication()).getCurrentPhotoUri();
            Config.selectedPhotos.add(uri.toString());
            Config.previewMedias.add(uri.toString());
            changeConfirmStatus();
            mPhotoAdapter.getDatas().add(0, new DavinciPhoto(uri));
            mPhotoAdapter.notifyDataSetChanged();
        }
    }

    private void setBinding() {
        // 设置照片Adapter
        mPhotoAdapter = new PhotoAdapter(this,
                new PhotoAdapter.OnPhotoSelectChangeListener() {
                    @Override
                    public void onChange() {
                        changeConfirmStatus();
                    }
                },
                new PhotoAdapter.OnCameraClickListener() {
                    @Override
                    public void onClick() {
                        if (Config.permissionHandler != null) {
                            if (Config.permissionHandler.requestPermission(DaVinci.PermissionType.CAMERA)) {
                                openCamera();
                            }
                        } else {
                            if (!PermissionsUtils.checkCameraPermission(PhotoActivity.this)) {
                                return;
                            }
                            openCamera();
                        }
                    }
                },
                new PhotoAdapter.OnImageClickListener() {
                    @Override
                    public void onClick(String path) {
                        // 更新预览数据
                        ArrayList<Object> list = new ArrayList<>();
                        for (DavinciPhoto photo : mPhotoAdapter.getDatas()) {
                            list.add(photo.uri.toString());
                        }
                        Config.previewMedias = list;
                        // 打开预览
                        DaVinci.preview(false)
                                .previewSelectable(true)
                                .start(PhotoActivity.this, path);
                    }
                });
        mVideoAdapter = new VideoAdapter(this,
                new VideoAdapter.OnVideoSelectChangeListener() {
                    @Override
                    public void onChange() {
                        changeConfirmStatus();
                    }
                },
                new VideoAdapter.OnVideoClickListener() {
                    @Override
                    public void onClick(DavinciVideo video) {
                        // 更新预览视频数据
                        Config.previewMedias = new ArrayList<>(mVideoAdapter.getDatas());
                        // 打开预览
                        DaVinci.preview(false)
                                .previewSelectable(true)
                                .start(PhotoActivity.this, video);
                    }
                });
        onSelectType(TYPE_IMAGE);
        // 监听相簿数据源变化
        mViewModel.albumList.observe(this, new Observer<ArrayList<Album>>() {
            @Override
            public void onChanged(ArrayList<Album> albums) {
                mBinding.album.setData(albums);
                selectAlbum(albums.get(0));
            }
        });
        // 预览Adapter
        mPreviewAdapter = new MediaPreviewAdapter(this, new MediaPreviewAdapter.OnDeleteClickListener() {
            @Override
            public void onDelete(int position) {
                if (mViewModel.selectType == TYPE_IMAGE) {
                    Config.selectedPhotos.remove(position);
                    mPhotoAdapter.notifyDataSetChanged();
                } else if (mViewModel.selectType == TYPE_VIDEO) {
                    Config.selectedVideos.remove(position);
                    mVideoAdapter.notifyDataSetChanged();
                }
                changeConfirmStatus();
            }
        });
        mBinding.rvSelect.setAdapter(mPreviewAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            int oldPosition;
            int newPosition;

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(ItemTouchHelper.DOWN | ItemTouchHelper.UP |
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, 0);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int lastOldPosition = viewHolder.getBindingAdapterPosition();
                if (oldPosition <= 0) {
                    oldPosition = lastOldPosition;
                }
                newPosition = target.getBindingAdapterPosition();
                mPreviewAdapter.notifyItemMoved(lastOldPosition, newPosition);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
                    if (oldPosition != newPosition) {
                        if (mViewModel.selectType == TYPE_IMAGE) {
                            Config.selectedPhotos.add(newPosition, Config.selectedPhotos.remove(oldPosition));
                            mPhotoAdapter.notifyDataSetChanged();
                        } else if (mViewModel.selectType == TYPE_VIDEO) {
                            Config.selectedVideos.add(newPosition, Config.selectedVideos.remove(oldPosition));
                            mVideoAdapter.notifyDataSetChanged();
                        }
                        oldPosition = 0;
                        newPosition = 0;
                    }
                }
                super.onSelectedChanged(viewHolder, actionState);
            }
        });
        itemTouchHelper.attachToRecyclerView(mBinding.rvSelect);
    }

    private void setListener() {
        mBinding.navigation.setListener(v -> onBackPressed(), v -> {
            if (mViewModel.albumList.getValue() == null || mViewModel.albumList.getValue().size() <= 0) {
                return;
            }
            if (mBinding.album.getVisibility() == View.GONE) {
                mBinding.album.openAlbum();
                mBinding.navigation.setArrowUp();
            } else {
                closeAlbum();
            }
        });
        mBinding.tvPhoto.setOnClickListener(v -> {
            if (mViewModel.albumList.getValue() == null || mViewModel.albumList.getValue().size() <= 0) {
                return;
            }
            onSelectType(TYPE_IMAGE);
        });
        mBinding.tvVideo.setOnClickListener(v -> {
            if (mViewModel.albumList.getValue() == null || mViewModel.albumList.getValue().size() <= 0) {
                return;
            }
            onSelectType(TYPE_VIDEO);
        });
        mBinding.tvConfirm.setOnClickListener(v -> finishAndSetResult());
        mBinding.tvOpenPermission.setOnClickListener(v -> {
            if (Config.permissionHandler != null && Config.permissionHandler.requestPermission(DaVinci.PermissionType.PHOTO)) {
                loadAlbum();
            }
        });
    }

    private void registerBus() {
        // 相簿选择
        LiveDataBus.getInstance().with(BusEvent.Photo.ALBUM_SELECT, Album.class).observe(this, new Observer<Album>() {
            @Override
            public void onChanged(Album album) {
                selectAlbum(album);
                closeAlbum();
            }
        });
        // 相簿空白位置点击关闭
        LiveDataBus.getInstance().with(BusEvent.Photo.ALBUM_BLANK_CLICK).observe(this, new Observer<Object>() {
            @Override
            public void onChanged(Object o) {
                closeAlbum();
            }
        });
    }

    private void loadAlbumWithPermission() {
        if (Config.permissionHandler != null) {
            if (Config.permissionHandler.requestPermission(DaVinci.PermissionType.PHOTO)) {
                loadAlbum();
            }
        } else {
            loadAlbum();
        }
    }

    private void loadAlbum() {
        mViewModel.permissionVisibility.setValue(View.GONE);
        AlbumHelper.loadAlbums(this, albums -> {
            Log.e("AlbumLoad", "onLoadFinished====" + albums.toString());
            mViewModel.albumList.setValue(albums);
            changeConfirmStatus();
        });
    }

    private void selectAlbum(Album album) {
        if (album == null) {
            return;
        }
        mBinding.navigation.setTitle(album.name);
        mPhotoAdapter.setDatas(album.photoList);
        mVideoAdapter.setDatas(album.videoList);
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
        if (mViewModel.selectType == TYPE_IMAGE) {
            mBinding.tvConfirm.setEnabled(Config.selectedPhotos.size() > 0);
            mViewModel.selectImageVisibility.setValue(Config.selectedPhotos.size() > 0 ? View.VISIBLE : View.GONE);
            mPreviewAdapter.setDatas(Config.selectedPhotos, false);
        } else {
            mBinding.tvConfirm.setEnabled(Config.selectedVideos.size() > 0);
            mViewModel.selectImageVisibility.setValue(Config.selectedVideos.size() > 0 ? View.VISIBLE : View.GONE);
            List<String> selectedVideos = new ArrayList<>();
            for (DavinciVideo video : Config.selectedVideos) {
                selectedVideos.add(video.uri.toString());
            }
            mPreviewAdapter.setDatas(selectedVideos, true);
        }
    }

    private void finishAndSetResult() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(DaVinci.ResultKey.KEY_SELECTED_PHOTOS, Config.selectedPhotos);
        intent.putParcelableArrayListExtra(DaVinci.ResultKey.KEY_SELECTED_VIDEOS, Config.selectedVideos);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void onSelectType(int type) {
        if (mViewModel.selectType == type) {
            return;
        }
        if (type == TYPE_IMAGE && Config.selectedVideos.size() > 0 || type == TYPE_VIDEO && Config.selectedPhotos.size() > 0) {
            DavinciToastUtils.showToast(this, getResources().getString(R.string.davinci_photo_video_cant_same_choice));
            return;
        }
        mViewModel.selectType = type;
        mBinding.tvPhoto.setSelected(mViewModel.selectType == TYPE_IMAGE);
        mBinding.tvPhoto.setTypeface(null, mViewModel.selectType == TYPE_IMAGE ? Typeface.BOLD : Typeface.NORMAL);
        mBinding.tvVideo.setSelected(mViewModel.selectType == TYPE_VIDEO);
        mBinding.tvVideo.setTypeface(null, mViewModel.selectType == TYPE_VIDEO ? Typeface.BOLD : Typeface.NORMAL);
        if (mViewModel.selectType == TYPE_IMAGE) {
            mBinding.rv.setAdapter(mPhotoAdapter);
        } else if (mViewModel.selectType == TYPE_VIDEO) {
            mBinding.rv.setAdapter(mVideoAdapter);
        }
    }
}
