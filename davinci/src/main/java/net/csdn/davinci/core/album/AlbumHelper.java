package net.csdn.davinci.core.album;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import net.csdn.davinci.Config;
import net.csdn.davinci.DaVinci;
import net.csdn.davinci.R;
import net.csdn.davinci.core.entity.Album;
import net.csdn.davinci.core.entity.DavinciPhoto;
import net.csdn.davinci.core.entity.DavinciVideo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class AlbumHelper {

    private static final String DIR_ALL_ID = "ALL";
    private static final String[] PROJECTION = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.DURATION
    };

    public static void loadAlbums(Context context, AlbumResultCallback callback) {
        if (context == null) {
            return;
        }
        // 过滤文件类型
        String selection;
        String[] selectionArgs;
        switch (Config.selectType) {
            case DaVinci.SelectType.VIDEO: {
                selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=? AND " + MediaStore.MediaColumns.SIZE + ">0";
                selectionArgs = new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)};
            }
            break;
            case DaVinci.SelectType.IMAGE_VIDEO: {
                selection = "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=? OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
                        + (Config.showGif ? "" : " AND " + MediaStore.MediaColumns.MIME_TYPE + "!=?")
                        + " AND " + MediaStore.MediaColumns.SIZE + ">0";
                if (Config.showGif) {
                    selectionArgs = new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE), String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)};
                } else {
                    selectionArgs = new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE), String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO), "image/gif"};
                }
            }
            break;
            case DaVinci.SelectType.IMAGE:
            default: {
                selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                        + (Config.showGif ? "" : " AND " + MediaStore.MediaColumns.MIME_TYPE + "!=?")
                        + " AND " + MediaStore.MediaColumns.SIZE + ">0";
                if (Config.showGif) {
                    selectionArgs = new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)};
                } else {
                    selectionArgs = new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE), "image/gif"};
                }
            }
        }
        String sortOrder = MediaStore.Files.FileColumns.DATE_ADDED + " DESC";
        Uri contentUri = MediaStore.Files.getContentUri("external");

        LinkedHashMap<String, Album> albums;
        Album albumAll;
        try (Cursor cursor = context.getContentResolver().query(contentUri, PROJECTION, selection, selectionArgs, sortOrder)) {
            // 获取到照片根据相册存储
            // 创建相册容器
            albums = new LinkedHashMap<>();
            // 自定义相册所有照片
            albumAll = new Album();
            albumAll.id = DIR_ALL_ID;
            albumAll.name = context.getString(R.string.davinci_all);
            // 添加所有照片到头部
            albums.put(DIR_ALL_ID, albumAll);
            while (cursor.moveToNext()) {
                int imageId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                Uri uri = ContentUris.withAppendedId(contentUri, imageId);
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                String albumId = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID));
                String albumName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
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
                if (duration <= 0) {
                    // 创建照片对象
                    DavinciPhoto photo = new DavinciPhoto();
                    photo.path = imagePath;
                    photo.uri = uri;
                    // 在当前相册和全部相册都添加照片
                    album.photoList.add(photo);
                    albumAll.photoList.add(photo);
                } else {
                    // 创建视频对象
                    DavinciVideo video = new DavinciVideo();
                    video.path = imagePath;
                    video.uri = uri;
                    video.duration = duration;
                    // 在当前相册和全部相册都添加照片
                    album.videoList.add(video);
                    albumAll.videoList.add(video);
                }
            }
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
