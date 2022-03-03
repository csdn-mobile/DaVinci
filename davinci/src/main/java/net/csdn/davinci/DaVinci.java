package net.csdn.davinci;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import net.csdn.davinci.listener.QrSacnCallback;
import net.csdn.davinci.ui.activity.PhotoActivity;
import net.csdn.davinci.ui.activity.PreviewActivity;

import java.util.ArrayList;

public class DaVinci {

    public final static String KEY_SELECTED_PHOTOS = "SELECTED_PHOTOS";

    private DaVinci() {
    }

    /**
     * 选择照片
     */
    public static DaVinciSelectBuilder select() {
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
        return new DaVinciPreviewBuilder(reset);
    }

    /**
     * 构造者接收参数
     */
    public static class DaVinciSelectBuilder {

        private DaVinciSelectBuilder() {
            Config.reset();
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
            Intent intent = new Intent(activity, PhotoActivity.class);
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 预览构造者
     */
    public static class DaVinciPreviewBuilder {

        private DaVinciPreviewBuilder(boolean reset) {
            if (reset) {
                Config.reset();
            }
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
         * 二维码扫描回调
         */
        public DaVinciPreviewBuilder qrScanCallback(QrSacnCallback callback) {
            Config.qrSacnCallback = callback;
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
}
