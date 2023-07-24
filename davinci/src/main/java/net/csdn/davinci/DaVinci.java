package net.csdn.davinci;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import net.csdn.davinci.core.entity.DavinciMedia;
import net.csdn.davinci.core.entity.DavinciPhoto;
import net.csdn.davinci.core.entity.DavinciVideo;
import net.csdn.davinci.core.interceptor.DavinciOnBackPressedInterceptor;
import net.csdn.davinci.core.permission.DavinciPermissionHandler;
import net.csdn.davinci.ui.activity.PhotoActivity;
import net.csdn.davinci.ui.activity.PreviewActivity;
import net.csdn.davinci.utils.PermissionsUtils;

import java.util.ArrayList;

public class DaVinci {

    public static class ResultKey {
        public final static String KEY_SELECTED_PHOTOS = "SELECTED_PHOTOS";
        public final static String KEY_SELECTED_VIDEOS = "SELECTED_VIDEOS";
    }

    public static class SelectType {
        public final static int IMAGE = 1000;
        public final static int VIDEO = 1001;
        public final static int IMAGE_VIDEO = 1002;
    }

    public static class PermissionType {
        public final static int PHOTO = 1000;
        public final static int CAMERA = 1001;
    }

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
         * 选择类型（照片、照片+视频、视频）
         * Davinci.SELECT_IMAGE
         * Davinci.SELECT_VIDEO
         * Davinci.SELECT_IMAGE_VIDEO
         */
        public DaVinciSelectBuilder selectType(int selectType) {
            Config.selectType = selectType;
            return this;
        }

        /**
         * 是否日间/夜间模式
         */
        public DaVinciSelectBuilder isDayStyle(boolean isDayStyle) {
            Config.isDayStyle = isDayStyle;
            return this;
        }

        /**
         * 已选中的图片（与视频互斥）
         */
        public DaVinciSelectBuilder selectedPhotos(ArrayList<DavinciPhoto> selectedPhotos) {
            Config.selectedPhotos = createNewArray(selectedPhotos);
            Config.selectedVideos = new ArrayList<>();
            return this;
        }

        /**
         * 已选中的视频（与图片互斥）
         */
        public DaVinciSelectBuilder selectedVideos(ArrayList<DavinciVideo> selectedVideos) {
            Config.selectedVideos = createNewArray(selectedVideos);
            Config.selectedPhotos = new ArrayList<>();
            return this;
        }

        /**
         * 权限请求
         */
        public DaVinciSelectBuilder permissionHandler(DavinciPermissionHandler permissionHandler) {
            Config.permissionHandler = permissionHandler;
            return this;
        }

        /**
         * 返回键判定
         */
        public DaVinciSelectBuilder onBackPressedInterceptor(DavinciOnBackPressedInterceptor backHandler) {
            Config.onBackPressedInterceptor = backHandler;
            return this;
        }

        /**
         * 打开相册浏览页面
         */
        public void start(Activity activity, int requestCode) {
            if (activity == null) {
                return;
            }
            if (Config.permissionHandler == null && !PermissionsUtils.checkReadStoragePermission(activity)) {
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
        public DaVinciPreviewBuilder selectedPhotos(ArrayList<DavinciPhoto> selectedPhotos) {
            Config.selectedPhotos = createNewArray(selectedPhotos);
            return this;
        }

        /**
         * 预览的的图片
         */
        public DaVinciPreviewBuilder previewPhotos(ArrayList<String> previewPhotos) {
            ArrayList<? super DavinciMedia> newList = new ArrayList<>();
            if (previewPhotos != null && previewPhotos.size() > 0) {
                for (String photoUrl : previewPhotos) {
                    DavinciPhoto photo;
                    if (photoUrl.startsWith("http")) {
                        photo = new DavinciPhoto(photoUrl);
                    } else {
                        Uri uri = Uri.parse(photoUrl);
                        photo = new DavinciPhoto(uri);
                        photo.path = uri.toString();
                    }
                    newList.add(photo);
                }
            }
            Config.previewMedias = newList;
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
            if (Config.previewMedias == null || Config.previewMedias.size() <= 0) {
                throw new IllegalArgumentException("Please set previewPhotos before preview");
            } else {
                start(activity, (DavinciMedia) Config.previewMedias.get(0));
            }
        }

        /**
         * 预览相册
         * media：当前浏览的图片地址 或者 视频的DavinciVideo
         */
        public void start(Activity activity, DavinciMedia media) {
            if (activity == null || media == null) {
                return;
            }
            if (media instanceof DavinciVideo || media instanceof DavinciPhoto) {
                Config.currentMedia = media;
            }
            Intent intent = new Intent(activity, PreviewActivity.class);
            activity.startActivity(intent);
        }
    }

    /**
     * 二维码扫描回调
     */
    public interface QrScanCallback {
        void onResult(String result);
    }

    /**
     * 创建新ArrayList，避免传入数据被改变
     */
    private static <T> ArrayList<T> createNewArray(ArrayList<T> oldList) {
        ArrayList<T> newList = new ArrayList<>();
        if (oldList != null && oldList.size() > 0) {
            newList.addAll(oldList);
        }
        return newList;
    }
}
