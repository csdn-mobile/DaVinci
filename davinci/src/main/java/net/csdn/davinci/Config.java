package net.csdn.davinci;

import net.csdn.davinci.core.engine.GlideEngine;
import net.csdn.davinci.core.engine.ImageEngine;
import net.csdn.davinci.core.entity.DavinciMedia;
import net.csdn.davinci.core.entity.DavinciPhoto;
import net.csdn.davinci.core.entity.DavinciVideo;
import net.csdn.davinci.core.interceptor.DavinciOnBackPressedInterceptor;
import net.csdn.davinci.core.permission.DavinciPermissionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
     * 选择类型（照片、照片+视频、视频）
     */
    public static int selectType;
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
     * 是否日间模式
     */
    public static boolean isDayStyle;
    /**
     * 图片加载引擎
     */
    public static ImageEngine imageEngine;
    /**
     * 已选中的照片
     */
    public static ArrayList<DavinciPhoto> selectedPhotos;
    /**
     * 已选中的视频
     */
    public static ArrayList<DavinciVideo> selectedVideos;
    /**
     * 当前预览的图片/视频地址
     */
    public static ArrayList<? super DavinciMedia> previewMedias;
    /**
     * 当前选中的照片/视频地址
     */
    public static DavinciMedia currentMedia;
    /**
     * 是否需要长按识别二维码
     */
    public static boolean needQrScan;
    /**
     * 二维码扫描回调
     */
    public static DaVinci.QrScanCallback qrScanCallback;
    /**
     * 网络图片缓存地址
     */
    public static Map<String, String> absolutePathMap;
    /**
     * 文件夹名称
     */
    public static String saveFolderName;
    /**
     * 权限请求相关
     */
    public static DavinciPermissionHandler permissionHandler;
    /**
     * 是否图库可返回
     */
    public static DavinciOnBackPressedInterceptor onBackPressedInterceptor;

    /**
     * 重置
     */
    public static void reset() {
        maxSelectable = 9;
        column = 4;
        showGif = false;
        showCamera = true;
        previewSelectable = false;
        selectType = DaVinci.SelectType.IMAGE;
        isDayStyle = true;
        selectedPhotos = new ArrayList<>();
        selectedVideos = new ArrayList<>();
        previewMedias = new ArrayList<>();
        currentMedia = null;
        imageEngine = new GlideEngine();
        needQrScan = true;
        qrScanCallback = null;
        absolutePathMap = new HashMap<>();
        saveFolderName = "CSDN";
        permissionHandler = null;
        onBackPressedInterceptor = null;
    }
}
