package net.csdn.davinci.ui.activity;

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
import net.csdn.davinci.R;
import net.csdn.davinci.core.album.AlbumClickListener;
import net.csdn.davinci.core.album.AlbumHelper;
import net.csdn.davinci.core.album.AlbumResultCallback;
import net.csdn.davinci.core.entity.Album;
import net.csdn.davinci.ui.adapter.PhotoAdapter;
import net.csdn.davinci.ui.view.EmptyView;
import net.csdn.davinci.ui.view.PhotoAlbum;
import net.csdn.davinci.ui.view.PhotoNavigation;

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
    protected void onDestroy() {
        super.onDestroy();
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
    }

    private void closeAlbum() {
        photoAlbum.closeAlbum();
        photoNavigation.setArrowDown();
    }
}
