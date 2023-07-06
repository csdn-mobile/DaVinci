package net.csdn.davinci.utils;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;

import net.csdn.davinci.ui.view.DavinciCenterToast;


/**
 * Created by KG on 2019/10/08.
 */

public class DavinciToastUtils {

    public static void showToast(Context context, String msg) {
        if (context == null || TextUtils.isEmpty(msg)) {
            return;
        }
        if (Looper.getMainLooper() != Looper.myLooper()) return;
        new DavinciCenterToast(context).showToast(msg);
    }
}
