package net.csdn.davinci;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import net.csdn.davinci.ui.activity.PhotoActivity;
import net.csdn.davinci.ui.activity.PreviewActivity;
import net.csdn.davinci.utils.PermissionsUtils;

import java.util.ArrayList;

public class DaVinci {

    public final static String KEY_SELECTED_PHOTOS = "SELECTED_PHOTOS";

    private DaVinci() {
    }

    /**
     * 选择照片
     */
    public static DaVinciSelectBuilder select() {
        Config.reset();
        return new DaVinciSelectBuilder();
    }

    /**
     * 预览照片
     */
    public static DaVinciPreviewBuilder preview() {
        return preview(true);
    }

    /**
     * 预览照片（是否重置设置）
     */
    public static DaVinciPreviewBuilder preview(boolean reset) {
        if (reset) {
            Config.reset();
        }
        return new DaVinciPreviewBuilder();
    }

    /**
     * 构造者接收参数
     */
    public static class DaVinciSelectBuilder {

        private DaVinciSelectBuilder() {
        }

        /**
         * 最多可选照片数
         */
        public DaVinciSelectBuilder maxSelectable(int maxSelectable) {
            Config.maxSelectable = maxSelectable;
            return this;
        }

        /**
         * 图片列数
         */
        public DaVinciSelectBuilder column(int column) {
            Config.column = column;
            return this;
        }

        /**
         * 是否显示GIF
         */
        public DaVinciSelectBuilder showGif(boolean showGif) {
            Config.showGif = showGif;
            return this;
        }

        /**
         * 是否显示相机
         */
        public DaVinciSelectBuilder showCamera(boolean showCamera) {
            Config.showCamera = showCamera;
            return this;
        }

        /**
         * 已选中的图片
         */
        public DaVinciSelectBuilder selectedPhotos(ArrayList<String> selectedPhotos) {
            Config.selectedPhotos = selectedPhotos == null ? new ArrayList<>() : selectedPhotos;
            return this;
        }

        /**
         * 打开相册浏览页面
         */
        public void start(Activity activity, int requestCode) {
            if (activity == null) {
                return;
            }
            if (!PermissionsUtils.checkReadStoragePermission(activity)) {
                Toast.makeText(activity, activity.getString(R.string.davinci_no_permission_read), Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = new Intent(activity, PhotoActivity.class);
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 预览构造者
     */
    public static class DaVinciPreviewBuilder {

        private DaVinciPreviewBuilder() {
        }

        /**
         * 是否预览可以选择图片
         */
        public DaVinciPreviewBuilder previewSelectable(boolean previewSelectable) {
            Config.previewSelectable = previewSelectable;
            return this;
        }

        /**
         * 已选中的图片
         */
        public DaVinciPreviewBuilder selectedPhotos(ArrayList<String> selectedPhotos) {
            Config.selectedPhotos = selectedPhotos == null ? new ArrayList<>() : selectedPhotos;
            return this;
        }

        /**
         * 预览的的图片
         */
        public DaVinciPreviewBuilder previewPhotos(ArrayList<String> selectedPhotos) {
            Config.previewPhotos = selectedPhotos == null ? new ArrayList<>() : selectedPhotos;
            return this;
        }

        /**
         * 是否需要识别二维码
         */
        public DaVinciPreviewBuilder needQrScan(boolean needQrScan) {
            Config.needQrScan = needQrScan;
            return this;
        }

        /**
         * 二维码扫描回调
         */
        public DaVinciPreviewBuilder qrScanCallback(QrScanCallback callback) {
            Config.qrScanCallback = callback;
            return this;
        }

        /**
         * 保存图片文件夹名称
         */
        public DaVinciPreviewBuilder saveFolderName(String folderName) {
            Config.saveFolderName = folderName;
            return this;
        }

        /**
         * 预览相册
         */
        public void start(Activity activity) {
            if (activity == null) {
                return;
            }
            if (Config.previewPhotos == null || Config.previewPhotos.size() <= 0) {
                throw new IllegalArgumentException("Please set previewPhotos before preview");
            } else {
                start(activity, Config.previewPhotos.get(0));
            }
        }

        /**
         * 预览相册
         * currentUri：当前浏览的图片地址
         */
        public void start(Activity activity, String currentUri) {
            if (activity == null) {
                return;
            }
            Config.currentPath = TextUtils.isEmpty(currentUri) ? "" : currentUri;
            Intent intent = new Intent(activity, PreviewActivity.class);
            activity.startActivityForResult(intent, PreviewActivity.RESULT_PREVIEW);
        }
    }

    /**
     * 二维码扫描回调
     */
    public interface QrScanCallback {
        void onResult(String result);
    }
}
