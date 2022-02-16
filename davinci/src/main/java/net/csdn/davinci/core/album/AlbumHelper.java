package net.csdn.davinci.core.album;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;

public class AlbumHelper {

    private static final int LOADER_ID = 120;

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
    }
}
