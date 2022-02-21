package net.csdn.davinci;

import net.csdn.davinci.core.engine.GlideEngine;
import net.csdn.davinci.core.engine.ImageEngine;

import java.util.ArrayList;

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
     * 是否预览可选择
     */
    public static boolean previewSelectable;
    /**
     * 图片加载引擎
     */
    public static ImageEngine imageEngine;
    /**
     * 已选中的照片
     */
    public static ArrayList<String> selectedPhotos;
    /**
     * 当前预览的图片地址
     */
    public static ArrayList<String> previewPhotos;
    /**
     * 当前选中的照片地址
     */
    public static String currentPath;

    /**
     * 重置
     */
    public static void reset() {
        maxSelectable = 9;
        column = 4;
        showGif = false;
        showCamera = true;
        previewSelectable = false;
        selectedPhotos = new ArrayList<>();
        previewPhotos = new ArrayList<>();
        currentPath = "";
        imageEngine = new GlideEngine();
    }
}
