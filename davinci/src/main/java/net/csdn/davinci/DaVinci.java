package net.csdn.davinci;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import net.csdn.davinci.ui.activity.PhotoActivity;
import net.csdn.davinci.ui.activity.PreviewActivity;

import java.util.ArrayList;
import java.util.List;

public class DaVinci {

    private DaVinci() {
    }

    public static DaVinciBuilder create() {
        return create(true);
    }

    public static DaVinciBuilder create(boolean reset) {
        return new DaVinciBuilder(reset);
    }

    /**
     * 构造者接收参数
     */
    public static class DaVinciBuilder {

        private DaVinciBuilder(boolean reset) {
            if (reset) {
                Config.reset();
            }
        }

        /**
         * 最多可选照片数
         */
        public DaVinciBuilder maxSelectable(int maxSelectable) {
            Config.maxSelectable = maxSelectable;
            return this;
        }

        /**
         * 图片列数
         */
        public DaVinciBuilder column(int column) {
            Config.column = column;
            return this;
        }

        /**
         * 是否显示GIF
         */
        public DaVinciBuilder showGif(boolean showGif) {
            Config.showGif = showGif;
            return this;
        }

        /**
         * 是否显示相机
         */
        public DaVinciBuilder showCamera(boolean showCamera) {
            Config.showCamera = showCamera;
            return this;
        }

        /**
         * 已选中的图片
         */
        public DaVinciBuilder selectedPhotos(List<String> selectedPhotos) {
            Config.selectedPhotos = selectedPhotos == null ? new ArrayList<>() : selectedPhotos;
            return this;
        }

        /**
         * 打开相册浏览页面
         */
        public void select(Activity activity, int requestCode) {
            if (activity == null) {
                return;
            }
            Intent intent = new Intent(activity, PhotoActivity.class);
            activity.startActivityForResult(intent, requestCode);
        }

        /**
         * 预览相册
         * currentUri：当前浏览的图片地址
         */
        public void preview(Activity activity, String currentUri) {
            if (activity == null) {
                return;
            }
            Config.currentUri = TextUtils.isEmpty(currentUri) ? "" : currentUri;
            Intent intent = new Intent(activity, PreviewActivity.class);
            activity.startActivity(intent);
        }
    }
}
