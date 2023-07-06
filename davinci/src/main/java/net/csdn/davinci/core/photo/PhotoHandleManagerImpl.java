package net.csdn.davinci.core.photo;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import net.csdn.davinci.Config;
import net.csdn.davinci.R;
import net.csdn.davinci.core.entity.SavePath;
import net.csdn.davinci.ui.dialog.PermissionsDialog;
import net.csdn.davinci.utils.DavinciToastUtils;
import net.csdn.davinci.utils.PermissionsUtils;
import net.csdn.davinci.utils.SystemUtils;
import net.csdn.davinci.utils.UrlUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

public class PhotoHandleManagerImpl implements PhotoHandleManager {

    private static final int MSG_GET_PATH = 11000;

    private PhotoHandler mHandler = new PhotoHandler();
    private Activity mActivity;
    private String mUrl;

    @SuppressLint("HandlerLeak")
    private class PhotoHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_GET_PATH) {
                SavePath data = (SavePath) msg.obj;
                if (!TextUtils.isEmpty(data.path)) {
                    Config.absolutePathMap.put(data.url, data.path);
                    analysisBitmap(data.url, data.path);
                } else {
                    analysisBitmap(data.url, null);
                }
            }
        }
    }

    public PhotoHandleManagerImpl(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * 显示保存图片、二维码弹窗
     */
    @Override
    public void showLongClickDialog(String url) {
        if (mActivity == null || TextUtils.isEmpty(url)) {
            return;
        }

        if (Config.absolutePathMap.containsKey(url)) {
            analysisBitmap(url, Config.absolutePathMap.get(url));
        } else {
            // 获取缓存图片本地地址
            new Thread(new Runnable() {
                @Override
                public void run() {
                    FutureTarget<File> future = Glide.with(mActivity)
                            .load(url)
                            .downloadOnly(100, 100);
                    String path = "";
                    try {
                        File cacheFile = future.get();
                        path = cacheFile.getAbsolutePath();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    SavePath data = new SavePath();
                    data.path = path;
                    data.url = url;

                    Message msg = Message.obtain();
                    msg.what = MSG_GET_PATH;
                    msg.obj = data;
                    mHandler.sendMessage(msg);
                }
            }).start();
        }
    }

    /**
     * 分析图片二维码
     */
    private void analysisBitmap(String url, String path) {
        if (!TextUtils.isEmpty(path) && Config.needQrScan) {
            CodeUtils.analyzeBitmap(path, new CodeUtils.AnalyzeCallback() {
                @Override
                public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                    if (mActivity.isFinishing() || TextUtils.isEmpty(result)) {
                        return;
                    }
                    // 先校验是不是网址
                    if (!UrlUtils.checkUrl(result)) {
                        showSelectDialog(url, null);
                        return;
                    }
                    // 如果没有http开头，加上https
                    if (!result.startsWith("http")) {
                        result = "https://" + result;
                    }
                    // 检验是否是正确网址
                    String host = UrlUtils.getHost(result);
                    if (TextUtils.isEmpty(host)) {
                        showSelectDialog(url, null);
                        return;
                    }
                    showSelectDialog(url, result);
                }

                @Override
                public void onAnalyzeFailed() {
                    showSelectDialog(url, null);
                }
            });
        } else {
            showSelectDialog(url, null);
        }
    }

    /**
     * 真正显示弹窗方法
     */
    private void showSelectDialog(String url, final String result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        String[] showMsgArr;
        if (TextUtils.isEmpty(result)) {
            showMsgArr = new String[]{"保存图片"};
        } else {
            showMsgArr = new String[]{"保存图片", "识别图中二维码"};
        }
        builder.setItems(showMsgArr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface mdialog, int which) {
                if (which == 0) {
                    // 图片下载
                    download(url);
                } else {
                    if (Config.qrScanCallback != null) {
                        Config.qrScanCallback.onResult(result);
                    }
                }
                if (SystemUtils.isActivityRunning(mActivity)) {
                    mdialog.dismiss();
                }
            }
        });
        if (SystemUtils.isActivityRunning(mActivity)) {
            builder.show();
        }
    }

    @Override
    public void download(String url) {
        if (!TextUtils.isEmpty(url)) {
            mUrl = url;
        }
        if (!PermissionsUtils.checkWriteStoragePermission(mActivity, false)) {
            PermissionsDialog dialog = new PermissionsDialog(PermissionsDialog.TYPE_STORAGE_WRITE, mActivity, new PermissionsDialog.OnButtonClickListener() {
                @Override
                public void onConfirmClick() {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (!mActivity.shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                            // 可以申请权限
                            ActivityCompat.requestPermissions(mActivity, PermissionsUtils.PERMISSIONS_EXTERNAL_WRITE, PermissionsUtils.REQUEST_EXTERNAL_WRITE);
                        } else {
                            // 跳转权限页面
                            PermissionsUtils.openPermissionPage(mActivity);
                        }
                    } else {
                        ActivityCompat.requestPermissions(mActivity, PermissionsUtils.PERMISSIONS_EXTERNAL_WRITE, PermissionsUtils.REQUEST_EXTERNAL_WRITE);
                    }
                }
            });
            dialog.show(mActivity);
        } else {
            startDownload(url);
        }
    }

    /**
     * 真正的下载方法
     */
    private void startDownload(String url) {
        String downloadUrl = "";
        if (!TextUtils.isEmpty(url)) {
            downloadUrl = url;
        } else if (!TextUtils.isEmpty(mUrl)) {
            downloadUrl = mUrl;
        }
        if (TextUtils.isEmpty(downloadUrl)) {
            return;
        }
        new DownloadAsyncTask(mActivity, downloadUrl).execute();
    }

    private static class DownloadAsyncTask extends AsyncTask<Void, Integer, Uri> {

        private final String mUrl;
        private WeakReference<Activity> mActivity;

        public DownloadAsyncTask(Activity activity, String url) {
            this.mUrl = url;
            this.mActivity = new WeakReference<>(activity);
        }

        @Override
        protected Uri doInBackground(Void... params) {
            Uri uri = null;
            try {
                Activity activity = mActivity.get();
                if (activity == null) {
                    return null;
                }
                // 获取文件类型
                String ext = "jpg";
                try {
                    Uri parseUri = Uri.parse(mUrl);
                    if (parseUri != null) {
                        String fileName = parseUri.getLastPathSegment();
                        if (!TextUtils.isEmpty(fileName)) {
                            if (fileName.endsWith("png")) {
                                ext = "png";
                            }
//                            else if (fileName.endsWith(".gif") || mUrl.endsWith("=gif")) {
//                                ext = "gif";
//                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Bitmap bitmap = Glide.with(activity)
                        .asBitmap()
                        .load(mUrl)
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
                uri = saveBitmap(activity, bitmap, Config.saveFolderName + "_" + System.currentTimeMillis(), ext);
            } catch (Exception e) {
                Log.e("SAVE_PICTURE", e.getMessage());
            }
            return uri;
        }

        @Override
        protected void onPostExecute(Uri file) {
            Activity activity = mActivity.get();
            if (activity == null) {
                return;
            }
            if (file == null) {
                DavinciToastUtils.showToast(activity, activity.getResources().getString(R.string.davinci_save_fail));
            } else {
                DavinciToastUtils.showToast(activity, activity.getResources().getString(R.string.davinci_save_success));
            }
        }

        public static Uri saveBitmap(Activity activity, Bitmap bm, String displayName, String ext) {
            try {
                displayName = displayName + "." + ext;
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
                values.put(MediaStore.MediaColumns.TITLE, displayName);
                values.put(MediaStore.MediaColumns.MIME_TYPE, "png".equals(ext) ? "image/png" : "image/jpeg");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    values.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/Camera");
                } else {
                    values.put(MediaStore.MediaColumns.DATA, Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Environment.DIRECTORY_DCIM + "/" + displayName);
                }
                Uri uri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                if (uri != null) {
                    OutputStream outputStream = activity.getContentResolver().openOutputStream(uri);
                    if (outputStream != null) {
                        bm.compress("png".equals(ext) ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.close();
                    }
                }
                return uri;
            } catch (IOException var6) {
                var6.printStackTrace();
                return null;
            }
        }
    }
}
