package net.csdn.davinci.core.album;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import net.csdn.davinci.R;
import net.csdn.davinci.core.entity.Album;
import net.csdn.davinci.core.entity.Photo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AlbumHelper {

    private static final int LOADER_ID = 120;
    private static final String DIR_ALL_ID = "ALL";

    private Context mContext;
    private LoaderManager mLoaderManager;

    public void onCreate(Activity activity) {
        if (activity == null) {
            return;
        }
        mContext = activity;
        mLoaderManager = activity.getLoaderManager();
    }

    public void onDestroy() {
        if (mLoaderManager == null) {
            return;
        }
        mLoaderManager.destroyLoader(LOADER_ID);
    }

    public void loadAlbums(AlbumResultCallback callback) {
        if (mLoaderManager == null || mContext == null) {
            return;
        }
        mLoaderManager.initLoader(LOADER_ID, null, new AlbumLoaderCallback(mContext, callback));

        String[] projections;
        Uri contentUri;
        String selection = null;
        String[] selectionAllArgs = null;
        final String sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC";
        contentUri = MediaStore.Files.getContentUri("external");
        selection =
                "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?" + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)";
        selectionAllArgs =
                new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
                        String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)};
        ContentResolver contentResolver = mContext.getContentResolver();
        List<String> projectionList = new ArrayList<String>();
        projectionList.add(MediaStore.Files.FileColumns._ID);
        projectionList.add(MediaStore.MediaColumns.DATA);
        projectionList.add(MediaStore.Images.Media.BUCKET_ID);
        projectionList.add(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME);
        projections = projectionList.toArray(new String[0]);
        Cursor cursor = contentResolver.query(contentUri, projections, selection,
                selectionAllArgs, sortOrder);
        // 获取到照片根据相册存储
        // 创建相册容器
        LinkedHashMap<String, Album> albums = new LinkedHashMap<>();
        // 自定义相册所有照片
        Album albumAll = new Album();
        albumAll.id = DIR_ALL_ID;
        albumAll.name = mContext.getString(R.string.davinci_all_image);
        // 添加所有照片到头部
        albums.put(DIR_ALL_ID, albumAll);
        while (cursor.moveToNext()) {
            int imageId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
            String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            String albumId = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID));
            String albumName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
            // 如果没创建相册对象就创建，创建过就获取
            Uri uri = ContentUris.withAppendedId(contentUri, imageId);
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
        callback.onResult(results);

    }

}
