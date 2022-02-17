package net.csdn.davinci.core.album;

import android.content.Context;
import android.content.CursorLoader;
import android.provider.MediaStore;
import android.util.Log;


import net.csdn.davinci.Config;
import net.csdn.davinci.utils.SystemUtils;

/**
 * 读取相册列表
 */
public class AlbumLoader extends CursorLoader {

    private static final String[] PROJECTION = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.MediaColumns.MIME_TYPE
    };

    public AlbumLoader(Context context) {
        super(context);
        setUri(MediaStore.Files.getContentUri("external"));
        setSortOrder(MediaStore.Images.Media.DATE_ADDED + " DESC");
        setProjection(PROJECTION);

        // 过滤文件类型
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                + (Config.showGif ? "" : " AND " + MediaStore.MediaColumns.MIME_TYPE + "!=?")
                + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                + (SystemUtils.underAndroidQ() ? ") GROUP BY (" + MediaStore.Images.Media.BUCKET_ID : "");
        Log.e("AlbumLoad", "selection====" + selection);
        setSelection(selection);

        // 参数
        String[] selectionArgs;
        if (Config.showGif) {
            selectionArgs = new String[]{
                    String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
            };
        } else {
            selectionArgs = new String[]{
                    String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
                    "image/gif"
            };
        }
        setSelectionArgs(selectionArgs);
    }
}