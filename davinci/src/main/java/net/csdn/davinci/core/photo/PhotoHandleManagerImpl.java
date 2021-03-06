package net.csdn.davinci.core.photo;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.bumptech.glide.request.target.Target.SIZE_ORIGINAL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import net.csdn.davinci.Config;
import net.csdn.davinci.R;
import net.csdn.davinci.core.entity.SavePath;
import net.csdn.davinci.ui.dialog.PermissionsDialog;
import net.csdn.davinci.utils.FileUtils;
import net.csdn.davinci.utils.PermissionsUtils;
import net.csdn.davinci.utils.SystemUtils;
import net.csdn.davinci.utils.UrlUtils;

import java.io.File;
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
     * ????????????????????????????????????
     */
    @Override
    public void showLongClickDialog(String url) {
        if (mActivity == null || TextUtils.isEmpty(url)) {
            return;
        }

        if (Config.absolutePathMap.containsKey(url)) {
            analysisBitmap(url, Config.absolutePathMap.get(url));
        } else {
            // ??????????????????????????????
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
     * ?????????????????????
     */
    private void analysisBitmap(String url, String path) {
        if (!TextUtils.isEmpty(path) && Config.needQrScan) {
            CodeUtils.analyzeBitmap(path, new CodeUtils.AnalyzeCallback() {
                @Override
                public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                    if (mActivity.isFinishing() || TextUtils.isEmpty(result)) {
                        return;
                    }
                    // ????????????????????????
                    if (!UrlUtils.checkUrl(result)) {
                        showSelectDialog(url, null);
                        return;
                    }
                    // ????????????http???????????????https
                    if (!result.startsWith("http")) {
                        result = "https://" + result;
                    }
                    // ???????????????????????????
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
     * ????????????????????????
     */
    private void showSelectDialog(String url, final String result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        String[] showMsgArr;
        if (TextUtils.isEmpty(result)) {
            showMsgArr = new String[]{"????????????"};
        } else {
            showMsgArr = new String[]{"????????????", "?????????????????????"};
        }
        builder.setItems(showMsgArr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface mdialog, int which) {
                if (which == 0) {
                    // ????????????
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
                            // ??????????????????
                            ActivityCompat.requestPermissions(mActivity, PermissionsUtils.PERMISSIONS_EXTERNAL_WRITE, PermissionsUtils.REQUEST_EXTERNAL_WRITE);
                        } else {
                            // ??????????????????
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
     * ?????????????????????
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

    private static class DownloadAsyncTask extends AsyncTask<Void, Integer, File> {

        private final String mUrl;
        private WeakReference<Activity> mActivity;

        public DownloadAsyncTask(Activity activity, String url) {
            this.mUrl = url;
            this.mActivity = new WeakReference<>(activity);
        }

        @Override
        protected File doInBackground(Void... params) {
            File file = null;
            try {
                Activity activity = mActivity.get();
                if (activity == null) {
                    return null;
                }

                FutureTarget<File> future = Glide
                        .with(activity)
                        .load(mUrl)
                        .downloadOnly(SIZE_ORIGINAL, SIZE_ORIGINAL);

                file = future.get();
                // ??????????????????
                File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();
                File appDir = new File(pictureFolder, Config.saveFolderName);
                if (!appDir.exists()) {
                    appDir.mkdirs();
                }
                String fileName = Config.saveFolderName + "_" + System.currentTimeMillis() + (mUrl.endsWith(".gif") || mUrl.endsWith("=gif") ? ".gif" : ".jpg");
                File destFile = new File(appDir, fileName);
                FileUtils.copy(file, destFile);
                // ????????????????????????
                activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(new File(destFile.getPath()))));
            } catch (Exception e) {
                Log.e("SAVE_PICTURE", e.getMessage());
            }
            return file;
        }

        @Override
        protected void onPostExecute(File file) {
            Activity activity = mActivity.get();
            if (activity == null) {
                return;
            }
            if (file == null) {
                Toast.makeText(activity, activity.getResources().getString(R.string.davinci_save_fail), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, activity.getResources().getString(R.string.davinci_save_success), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
