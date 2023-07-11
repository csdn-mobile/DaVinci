package net.csdn.davinci.ui.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.csdn.davinci.R;

/**
 * Created by KG on 2019/6/18.
 */
public class DavinciCenterToast extends Toast {

    private Context mContext;
    private TextView tvContent;

    public DavinciCenterToast(Context context) {
        super(context);
        mContext = context;
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.davinci_view_davinci_center_toast, null);
        tvContent = view.findViewById(R.id.tv_content);
        setDuration(Toast.LENGTH_SHORT);
        setGravity(Gravity.CENTER, 0, 0);
        setView(view);
    }

    public void showToast(String content) {
        try {
            tvContent.setText(content);
            show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
