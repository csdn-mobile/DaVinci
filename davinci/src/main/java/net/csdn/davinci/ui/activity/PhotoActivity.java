package net.csdn.davinci.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csdn.statusbar.StatusBar;
import com.csdn.statusbar.annotation.FontMode;

import net.csdn.davinci.Config;
import net.csdn.davinci.DaVinci;
import net.csdn.davinci.R;
import net.csdn.davinci.core.album.AlbumClickListener;
import net.csdn.davinci.core.album.AlbumHelper;
import net.csdn.davinci.core.album.AlbumResultCallback;
import net.csdn.davinci.core.entity.Album;
import net.csdn.davinci.core.entity.Photo;
import net.csdn.davinci.core.photo.PhotoCaptureManager;
import net.csdn.davinci.ui.adapter.PhotoAdapter;
import net.csdn.davinci.ui.view.EmptyView;
import net.csdn.davinci.ui.view.PhotoAlbum;
import net.csdn.davinci.ui.view.PhotoNavigation;
import net.csdn.davinci.utils.PermissionsUtils;

import java.io.IOException;
import java.util.ArrayList;

public class PhotoActivity extends AppCompatActivity {

    private RecyclerView rv;
    private EmptyView emptyView;
    private PhotoAlbum photoAlbum;
    private PhotoNavigation photoNavigation;

    private AlbumHelper mAlbumHelper;
    private PhotoAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        StatusBar.Builder()
                .color(getResources().getColor(R.color.davinci_white))
                .fontMode(FontMode.DARK)
                .change(this);

        rv = findViewById(R.id.rv);
        emptyView = findViewById(R.id.empty);
        photoAlbum = findViewById(R.id.album);
        photoNavigation = findViewById(R.id.navigation);

        setListener();
        loadAlbum();
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
        Config.reset();
        if (mAlbumHelper != null) {
            mAlbumHelper.onDestroy();
        }
    }

    @Override
    public void onBackPressed() {
        if (photoAlbum.getVisibility() == View.VISIBLE) {
            photoAlbum.closeAlbum();
            photoNavigation.setArrowDown();
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
                String path = PhotoCaptureManager.getInstance(getApplication()).getCurrentPhotoPath();
                Config.selectedPhotos.add(path);
                mAdapter.getDatas().add(0, new Photo(path));
                mAdapter.notifyDataSetChanged();
            }
            break;
            case PreviewActivity.RESULT_PREVIEW: {
                finishAndSetResult();
            }
            break;
        }
    }

    private void setListener() {
        photoNavigation.setOnBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        photoNavigation.setOnTitleClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoAlbum.getVisibility() == View.GONE) {
                    photoAlbum.openAlbum();
                    photoNavigation.setArrowUp();
                } else {
                    closeAlbum();
                }
            }
        });

        photoNavigation.setOnConfirmClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAndSetResult();
            }
        });

        photoAlbum.setOnBlankClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAlbum();
            }
        });

        photoAlbum.setAlbumClickListener(new AlbumClickListener() {
            @Override
            public void onAlbumClick(Album album) {
                selectAlbum(album);
                closeAlbum();
            }
        });
    }

    private void loadAlbum() {
        mAdapter = new PhotoAdapter(this, new PhotoAdapter.OnPhotoSelectChangeListener() {
            @Override
            public void onChange() {
                if (Config.selectedPhotos.size() <= 0) {
                    photoNavigation.setDoUnEnable();
                } else {
                    photoNavigation.setDoEnable();
                }
            }
        }, new PhotoAdapter.OnCameraClickListener() {
            @Override
            public void onClick() {
                if (!PermissionsUtils.checkCameraPermission(PhotoActivity.this)) {
                    return;
                }
                if (!PermissionsUtils.checkWriteStoragePermission(PhotoActivity.this)) {
                    return;
                }
                openCamera();
            }
        });
        rv.setLayoutManager(new GridLayoutManager(this, Config.column));
        rv.setAdapter(mAdapter);

        // 读取相簿
        mAlbumHelper = new AlbumHelper();
        mAlbumHelper.onCreate(this);
        mAlbumHelper.loadAlbums(new AlbumResultCallback() {
            @Override
            public void onResult(ArrayList<Album> albums) {
                Log.e("AlbumLoad", "onLoadFinished====" + albums.toString());
                photoAlbum.setData(albums);
                selectAlbum(albums.get(0));
            }
        });

    }

    private void selectAlbum(Album album) {
        if (photoNavigation == null || album == null) {
            return;
        }
        photoNavigation.setTitle(album.name);
        mAdapter.setDatas(album.photoList);

        ArrayList<String> list = new ArrayList<>();
        for (Photo photo : album.photoList) {
            list.add(photo.imgPath);
        }
        Config.previewPhotos = list;
    }

    private void closeAlbum() {
        photoAlbum.closeAlbum();
        photoNavigation.setArrowDown();
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

    private void finishAndSetResult() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(DaVinci.KEY_SELECTED_PHOTOS, Config.selectedPhotos);
        setResult(RESULT_OK, intent);
        finish();
    }

}
