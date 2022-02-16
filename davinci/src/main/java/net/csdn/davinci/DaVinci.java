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
        return new DaVinciBuilder();
    }

    /**
     * 构造者接收参数
     */
    public static class DaVinciBuilder {

        private DaVinciBuilder() {
            Parameter.reset();
        }

        /**
         * 最多可选照片数
         */
        public DaVinciBuilder maxSelectable(int maxSelectable) {
            Parameter.maxSelectable = maxSelectable;
            return this;
        }

        /**
         * 图片列数
         */
        public DaVinciBuilder column(int column) {
            Parameter.column = column;
            return this;
        }

        /**
         * 是否显示GIF
         */
        public DaVinciBuilder showGif(boolean showGif){
            Parameter.showGif = showGif;
            return this;
        }

        /**
         * 已选中的图片
         */
        public DaVinciBuilder selectedPhotos(List<String> selectedPhotos) {
            Parameter.selectedPhotos = selectedPhotos == null ? new ArrayList<>() : selectedPhotos;
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
            Parameter.currentUri = TextUtils.isEmpty(currentUri) ? "" : currentUri;
            Intent intent = new Intent(activity, PreviewActivity.class);
            activity.startActivity(intent);
        }
    }
}
