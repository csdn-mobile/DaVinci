package net.csdn.davinci.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import net.csdn.davinci.R;
import net.csdn.davinci.utils.SystemUtils;


/**
 * 权限前置弹窗
 */
public class PermissionsDialog extends Dialog {

    public static final int TYPE_STORAGE_READ = 10000;
    public static final int TYPE_STORAGE_WRITE = 10001;
    public static final int TYPE_CAMERA = 10002;

    private OnButtonClickListener mListener;
    private String mTitle;
    private String mDescribe;
    int mType;

    public interface OnButtonClickListener {
        /**
         * 点击确认按钮
         */
        void onConfirmClick();
    }

    public PermissionsDialog(int type, @NonNull Context context, OnButtonClickListener listener) {
        this(context, R.style.DaVinciDialog);
        this.mType = type;
        this.mListener = listener;

        init();
    }

    private PermissionsDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    private void init() {
        setContentView(R.layout.dialog_permissions);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        LinearLayout llCancel = findViewById(R.id.ll_cancel);
        LinearLayout llOk = findViewById(R.id.ll_ok);
        TextView tvDesc = findViewById(R.id.tv_desc);
        TextView tvTitle = findViewById(R.id.tv_title);

        findText();
        tvTitle.setText(mTitle);
        tvDesc.setText(mDescribe);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mListener != null) {
                    mListener.onConfirmClick();
                }
            }
        });
        llCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void findText() {
        String appName = SystemUtils.getAppName(getContext());
        if (!TextUtils.isEmpty(appName)) {
            appName = "\"" + appName + "\"";
        } else {
            appName = "";
        }
        switch (mType) {
            case TYPE_CAMERA:
                mTitle = getContext().getString(R.string.davinci_permission_title_camera);
                mDescribe = getContext().getString(R.string.davinci_permission_desc_camera, appName);
                break;
            case TYPE_STORAGE_READ:
                mTitle = getContext().getString(R.string.davinci_permission_title_read);
                mDescribe = getContext().getString(R.string.davinci_permission_desc_read, appName);
                break;
            case TYPE_STORAGE_WRITE:
                mTitle = getContext().getString(R.string.davinci_permission_title_write);
                mDescribe = getContext().getString(R.string.davinci_permission_desc_write, appName);
                break;
            default:
        }
    }

    public void show(Activity activity) {
        if (SystemUtils.isActivityRunning(activity)) {
            super.show();
        }
    }
}
