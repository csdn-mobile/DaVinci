package net.csdn.davinci.core.photo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import net.csdn.davinci.Config;
import net.csdn.davinci.core.entity.SavePath;
import net.csdn.davinci.utils.UrlUtils;

import java.io.File;

public class PhotoHandleManagerImpl implements PhotoHandleManager {

    private static final int MSG_GET_PATH = 11000;

    private PhotoHandler mHandler = new PhotoHandler();
    private Activity mActivity;

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
    public void showDialog(String url) {
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
        if (!TextUtils.isEmpty(path)) {
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
                    download(mActivity, url);
                } else {
                    if (Config.qrSacnCallback != null) {
                        Config.qrSacnCallback.onResult(result);
                    }
                }
                mdialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void download(Context context, String url) {

    }
}
