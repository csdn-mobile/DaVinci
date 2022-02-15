package net.csdn.davinci.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.csdn.davinci.R;

public class PhotoNavigation extends RelativeLayout {

    private ImageView ivBack;
    private LinearLayout llTitle;
    private TextView tvTitle;
    private ImageView ivArrow;
    private TextView tvDo;

    public PhotoNavigation(Context context) {
        this(context, null);
    }

    public PhotoNavigation(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoNavigation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_photo_navigation, this);

        ivBack = view.findViewById(R.id.iv_back);
        llTitle = view.findViewById(R.id.ll_title);
        tvTitle = view.findViewById(R.id.tv_title);
        ivArrow = view.findViewById(R.id.iv_arrow);
        tvDo = view.findViewById(R.id.tv_do);
    }

    /**
     * 设置标题
     */
    public void setTitle(String title) {
        if (tvTitle == null || TextUtils.isEmpty(title)) {
            return;
        }
        tvTitle.setText(title);
    }

    /**
     * 设置向上箭头
     */
    public void setArrowUp() {
        if (ivArrow == null) {
            return;
        }
        ivArrow.setImageResource(R.drawable.davinci_arrow_up);
    }

    /**
     * 设置向下箭头
     */
    public void setArrowDown() {
        if (ivArrow == null) {
            return;
        }
        ivArrow.setImageResource(R.drawable.davinci_arrow_down);
    }

    /**
     * 设置确定按钮可用
     */
    public void setDoEnable() {
        if (tvDo == null) {
            return;
        }
        tvDo.setEnabled(true);
        tvDo.setTextColor(getResources().getColor(R.color.davinci_confirm));
    }

    /**
     * 设置确定按钮不可用
     */
    public void setDoUnEnable() {
        if (tvDo == null) {
            return;
        }
        tvDo.setEnabled(false);
        tvDo.setTextColor(getResources().getColor(R.color.davinci_unenable));
    }

    /**
     * 设置返回键点击事件
     */
    public void setOnBackClick(OnClickListener listener) {
        if (ivBack == null || listener == null) {
            return;
        }
        ivBack.setOnClickListener(listener);
    }

    /**
     * 设置标题点击事件
     */
    public void setOnTitleClick(OnClickListener listener) {
        if (llTitle == null || listener == null) {
            return;
        }
        llTitle.setOnClickListener(listener);
    }
}
