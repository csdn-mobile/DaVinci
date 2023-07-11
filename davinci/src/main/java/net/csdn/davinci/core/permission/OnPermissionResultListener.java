package net.csdn.davinci.core.permission;

public interface OnPermissionResultListener {

    /**
     * 相册权限请求成功
     */
    void onPhotoPermissionSuccess();

    /**
     * 相机权限请求成功
     */
    void onCameraPermissionSuccess();
}
