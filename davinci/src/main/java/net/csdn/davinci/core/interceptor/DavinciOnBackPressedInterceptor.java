package net.csdn.davinci.core.interceptor;

import android.app.Activity;

public interface DavinciOnBackPressedInterceptor {

    /**
     * 请求返回是否可以退出选择相册界面
     * （将返回的控制权交给APP，能够处理额外弹窗等逻辑）
     *
     * @return 是否可以返回
     */
    boolean onBackPressed(Activity activity);
}
