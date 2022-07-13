package net.csdn.davinci.core.album;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.csdn.davinci.R;
import net.csdn.davinci.core.entity.Album;
import net.csdn.davinci.core.entity.Photo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class AlbumLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String DIR_ALL_ID = "ALL";

    private final Context mContext;
    private AlbumResultCallback mCallback;

    public AlbumLoaderCallback(@NonNull Context context, AlbumResultCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Log.e("AlbumLoad", "onCreateLoader====" + id);
        return new AlbumLoader(mContext);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data == null) {
            return;
        }
        // 获取到照片根据相册存储
        // 创建相册容器
        LinkedHashMap<String, Album> albums = new LinkedHashMap<>();
        // 自定义相册所有照片
        Album albumAll = new Album();
        albumAll.id = DIR_ALL_ID;
        albumAll.name = mContext.getString(R.string.davinci_all_image);
        // 添加所有照片到头部
        albums.put(DIR_ALL_ID, albumAll);
        Uri external = MediaStore.Files.getContentUri("external");
        while (data.moveToNext()) {
            int imageId = data.getInt(data.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
            Uri uri = ContentUris.withAppendedId(external, imageId);
            String imagePath = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            String albumId = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID));
            String albumName = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
            // 如果没创建相册对象就创建，创建过就获取
            Album album;
            if (!albums.containsKey(albumId)) {
                album = new Album();
                album.id = albumId;
                album.name = albumName;
                album.coverPath = imagePath;
                album.uri = uri;
                albums.put(albumId, album);
            } else {
                album = albums.get(albumId);
                if (album == null) {
                    album = new Album();
                    albums.put(albumId, album);
                }
            }
            // 创建照片对象
            Photo photo = new Photo();
            photo.id = String.valueOf(imageId);
            photo.imgPath = imagePath;
            photo.uri = uri;
            // 在当前相册和全部相册都添加照片
            album.photoList.add(photo);
            albumAll.photoList.add(photo);
        }

        ArrayList<Album> results = new ArrayList<>();
        for (Map.Entry<String, Album> entry : albums.entrySet()) {
            results.add(entry.getValue());
        }
        albumAll.coverPath = results.size() <= 1 ? "" : results.get(1).coverPath;
        albumAll.uri = results.size() <= 1 ? null : results.get(1).uri;
        mCallback.onResult(results);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
