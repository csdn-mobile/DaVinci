package net.csdn.davinci.core.permission;

public interface DavinciPermissionHandler {

    /**
     * 根据类型请求权限
     *
     * @param type 请求权限的类型(Davinci.PermissionType.PHOTO | Davinci.PermissionType.CAMERA)
     * @return 是否权限允许
     */
    boolean requestPermission(int type);
}
