package net.csdn.davinci;

import java.util.ArrayList;
import java.util.List;

public class Parameter {

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
     * 已选中的照片
     */
    public static List<String> selectedPhotos;
    /**
     * 当前选中的照片地址
     */
    public static String currentUri;

    /**
     * 重置
     */
    public static void reset(){
        maxSelectable = 9;
        column = 4;
        showGif = false;
        selectedPhotos = new ArrayList<>();
        currentUri = "";
    }
}
