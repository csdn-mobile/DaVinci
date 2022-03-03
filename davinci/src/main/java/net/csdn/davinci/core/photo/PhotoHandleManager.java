package net.csdn.davinci.core.photo;

import android.content.Context;

public interface PhotoHandleManager {

    void showDialog(String url);

    void download(Context context, String url);
}
