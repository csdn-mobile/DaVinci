package net.csdn.davinci;

import net.csdn.davinci.core.engine.GlideEngine;
import net.csdn.davinci.core.engine.ImageEngine;

import java.util.ArrayList;
import java.util.List;

public class Config {

    /**
     * 最大可选张数
     */
    public static int maxSelectable;
    /**
     * 列数
     */
    public static int column;
    /**
     * 是否显示GIF
     */
    public static boolean showGif;
    /**
     * 是否显示相机
     */
    public static boolean showCamera;
    /**
     * 已选中的照片
     */
    public static List<String> selectedPhotos;
    /**
     * 当前选中的照片地址
     */
    public static String currentPath;
    /**
     * 图片加载引擎
     */
    public static ImageEngine imageEngine;

    /**
     * 重置
     */
    public static void reset() {
        maxSelectable = 9;
        column = 4;
        showGif = false;
        showCamera = true;
        selectedPhotos = new ArrayList<>();
        currentPath = "";
        imageEngine = new GlideEngine();
    }
}
