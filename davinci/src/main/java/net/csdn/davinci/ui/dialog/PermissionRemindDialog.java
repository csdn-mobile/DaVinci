package net.csdn.davinci.ui.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import net.csdn.davinci.R;


public class PermissionRemindDialog extends Dialog implements View.OnClickListener {
    private final Context mContext;
    private TextView tvDescribe;
    private OnPermissionRemindListener mOnPermissionRemindListener;

    private PermissionRemindDialog(Builder builder) {
        this(builder, R.style.AffirmDialog);
    }

    private PermissionRemindDialog(Builder builder, int themeResId) {
        super(builder.mContext, themeResId);
        mContext = builder.mContext;
        mOnPermissionRemindListener = builder.mOnPermissionRemindListener;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_remind_permissions);
        tvDescribe = findViewById(R.id.tv_describe);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.next).setOnClickListener(this);

        readFunctions();

    }

    private void readFunctions() {
        //获取id对应的功能描述
        StringBuilder desc = new StringBuilder();

        //拼接头信息
        desc.append("「")
                .append("保存图片")
                .append("」需要您开启以下系统权限\n\n");

        desc.append("- ").append("文件存储及访问").append("权限:").append("用于长按保存博客中的图片").append("\n");
        desc.append("\n").append("您可以在【设置】-【隐私设置】-【权限设置】中随时关闭该权限。");

        tvDescribe.setText(desc);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cancel) {
            if (mOnPermissionRemindListener != null) {
                mOnPermissionRemindListener.onCancel();
            }
            dismissDialog();
        } else if (id == R.id.next) {
            if (mOnPermissionRemindListener != null) {
                mOnPermissionRemindListener.onNext();
            }
            dismissDialog();
        }
    }

    public void showDialog() {
        if (mContext != null) {
            if (mContext instanceof Activity) {
                Activity activity = (Activity) mContext;
                if (activity.isDestroyed() || activity.isFinishing()) {
                    return;
                }
            }
            try {
                if (!isShowing()) {
                    show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dismissDialog() {
        if (mContext != null) {
            if (mContext instanceof Activity) {
                Activity activity = (Activity) mContext;
                if (activity.isDestroyed() || activity.isFinishing()) {
                    return;
                }
            }
            try {
                if (isShowing()) {
                    dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class Builder {
        String[] mFunctions;
        String[] mPermissions;
        Context mContext;
        OnPermissionRemindListener mOnPermissionRemindListener;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder functions(String[] functions) {
            mFunctions = functions;
            return this;
        }

        public Builder permissions(String[] permission) {
            mPermissions = permission;
            return this;
        }

        public Builder listener(OnPermissionRemindListener listener) {
            mOnPermissionRemindListener = listener;
            return this;
        }

        public PermissionRemindDialog build() {
            return new PermissionRemindDialog(this);
        }
    }
}
