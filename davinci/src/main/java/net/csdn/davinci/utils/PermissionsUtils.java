package net.csdn.davinci.utils;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.READ_MEDIA_VIDEO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import net.csdn.davinci.ui.dialog.PermissionsDialog;

public class PermissionsUtils {
    public static final int REQUEST_CAMERA = 1;
    public static final int REQUEST_EXTERNAL_READ = 2;
    public static final int REQUEST_EXTERNAL_WRITE = 3;

    public static final String[] PERMISSIONS_CAMERA = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static final String[] PERMISSIONS_EXTERNAL_WRITE =
            Build.VERSION.SDK_INT >= 33 ? new String[]{
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.READ_MEDIA_VIDEO} : new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static final String[] PERMISSIONS_EXTERNAL_READ = Build.VERSION.SDK_INT >= 33 ? new String[]{
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO} : new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * 检测读权限
     */
    public static boolean checkReadStoragePermission(Activity activity) {
        boolean readStoragePermissionGranted = false;
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(activity, READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                    && (ContextCompat.checkSelfPermission(activity, READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED)) {
                readStoragePermissionGranted = true;
            }
        } else {
            if (ContextCompat.checkSelfPermission(activity, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                readStoragePermissionGranted = true;
            }
        }
        if (!readStoragePermissionGranted) {
            PermissionsDialog dialog = new PermissionsDialog(PermissionsDialog.TYPE_STORAGE_READ, activity, new PermissionsDialog.OnButtonClickListener() {
                @Override
                public void onConfirmClick() {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (!activity.shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                            // 可以申请权限
                            ActivityCompat.requestPermissions(activity, PERMISSIONS_EXTERNAL_READ, REQUEST_EXTERNAL_READ);
                        } else {
                            // 跳转权限页面
                            openPermissionPage(activity);
                        }
                    } else {
                        ActivityCompat.requestPermissions(activity, PERMISSIONS_EXTERNAL_READ, REQUEST_EXTERNAL_READ);
                    }
                }
            });
            dialog.show(activity);
        }
        return readStoragePermissionGranted;
    }

    /**
     * 检测写权限
     */
    public static boolean checkWriteStoragePermission(Activity activity, boolean isRequest) {
        if (Build.VERSION.SDK_INT < 33) {
        int writeStoragePermissionState = ContextCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE);
        boolean writeStoragePermissionGranted = writeStoragePermissionState == PackageManager.PERMISSION_GRANTED;
        if (!writeStoragePermissionGranted && isRequest) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_EXTERNAL_WRITE, REQUEST_EXTERNAL_WRITE);
        }
            return writeStoragePermissionGranted;
        }
        return true;
    }

    /**
     * 检测相机权限
     */
    public static boolean checkCameraPermission(Activity activity) {
        int cameraPermissionState = ContextCompat.checkSelfPermission(activity, CAMERA);
        int writePermissionState = ContextCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE);
        boolean cameraPermissionGranted = (cameraPermissionState == PackageManager.PERMISSION_GRANTED) && (writePermissionState == PackageManager.PERMISSION_GRANTED);
        if (!cameraPermissionGranted) {
            PermissionsDialog dialog = new PermissionsDialog(PermissionsDialog.TYPE_CAMERA, activity, new PermissionsDialog.OnButtonClickListener() {
                @Override
                public void onConfirmClick() {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (!activity.shouldShowRequestPermissionRationale(CAMERA)) {
                            // 可以申请权限
                            ActivityCompat.requestPermissions(activity, PERMISSIONS_CAMERA, REQUEST_CAMERA);
                        } else {
                            // 跳转权限页面
                            openPermissionPage(activity);
                        }
                    } else {
                        ActivityCompat.requestPermissions(activity, PERMISSIONS_CAMERA, REQUEST_CAMERA);
                    }
                }
            });
            dialog.show(activity);
        }
        return cameraPermissionGranted;
    }

    /**
     * 跳转手机权限详情
     */
    public static void openPermissionPage(Context context) {
        String brand = Build.BRAND;//手机厂商
        if (TextUtils.equals(brand.toLowerCase(), "redmi") || TextUtils.equals(brand.toLowerCase(), "xiaomi")) {
            gotoMiuiPermission(context);//小米
        } else if (TextUtils.equals(brand.toLowerCase(), "meizu")) {
            gotoMeizuPermission(context);
        } else if (TextUtils.equals(brand.toLowerCase(), "huawei") || TextUtils.equals(brand.toLowerCase(), "honor")) {
            gotoHuaweiPermission(context);
        } else {
            context.startActivity(getAppDetailSettingIntent(context));
        }
    }

    /**
     * 跳转到miui的权限管理页面
     */
    private static void gotoMiuiPermission(Context context) {
        try { // MIUI 8
            Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", context.getPackageName());
            context.startActivity(localIntent);
        } catch (Exception e) {
            try { // MIUI 5/6/7
                Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                localIntent.putExtra("extra_pkgname", context.getPackageName());
                context.startActivity(localIntent);
            } catch (Exception e1) { // 否则跳转到应用详情
                context.startActivity(getAppDetailSettingIntent(context));
            }
        }
    }

    /**
     * 跳转到魅族的权限管理系统
     */
    private static void gotoMeizuPermission(Context context) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", context.getPackageName());
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            context.startActivity(getAppDetailSettingIntent(context));
        }
    }

    /**
     * 华为的权限管理页面
     */
    private static void gotoHuaweiPermission(Context context) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            context.startActivity(getAppDetailSettingIntent(context));
        }
    }

    /**
     * 获取应用详情页面intent（如果找不到要跳转的界面，也可以先把用户引导到系统设置页面）
     */
    private static Intent getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        return localIntent;
    }

}
