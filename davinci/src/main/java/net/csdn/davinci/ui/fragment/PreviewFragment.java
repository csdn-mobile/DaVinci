package net.csdn.davinci.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import net.csdn.davinci.Config;
import net.csdn.davinci.R;
import net.csdn.davinci.core.photoview.PhotoView;
import net.csdn.davinci.utils.PhotoUtils;

import java.io.File;

public class PreviewFragment extends Fragment {

    private static final String ARGS_ITEM = "args_item";
    private static final String SAVE_PATH = "save_path";
    private static final int HANDLER_LOAD_LONG = 1100;

    private PhotoView iv;
    private ProgressBar progressBar;

    private String mPath;
    private OnPhotoClickListener mListener;
    private PreviewHandler mHandler = new PreviewHandler();

    public interface OnPhotoClickListener {
        void onSingleClick();

        void onLongClick(String url);
    }

    public static PreviewFragment newInstance(String path) {
        PreviewFragment fragment = new PreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_ITEM, path);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null && TextUtils.isEmpty(mPath)) {
            mPath = savedInstanceState.getString(SAVE_PATH);
        } else {
            if (getArguments() == null) {
                return;
            }
            mPath = getArguments().getString(ARGS_ITEM);
            if (mPath == null) {
                return;
            }
        }

        iv = view.findViewById(R.id.iv);
        progressBar = view.findViewById(R.id.progress_bar);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onSingleClick();
                }
            }
        });
        iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mListener != null) {
                    mListener.onLongClick(mPath);
                }
                return false;
            }
        });
        if (mPath.startsWith("http")) {
            progressBar.setVisibility(View.VISIBLE);
            Config.imageEngine.loadNetImage(getContext(), iv, mPath, new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    // 不要调整顺序！！！！
                    iv.setImageDrawable(resource);
                    int height = resource.getBounds().height();
                    int width = resource.getBounds().width();
                    boolean longerImage = isLongerImage(width, height);
                    if (!longerImage) {
                        iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                    // 设置长图缩放
                    iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    mHandler.sendEmptyMessage(HANDLER_LOAD_LONG);
                    return true;
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            Point originSize = PhotoUtils.getOriginSize(mPath, getActivity());
            if (!isLongerImage(originSize.x, originSize.y)) {
                Point size = PhotoUtils.getBitmapSize(Uri.fromFile(new File(mPath)), getActivity());
                iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Config.imageEngine.loadLocalImage(getContext(), size.x, size.y, iv, mPath);
            } else {
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Config.imageEngine.loadLocalLongImage(getContext(), iv, mPath);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!TextUtils.isEmpty(mPath)) {
            outState.putString(SAVE_PATH, mPath);
        }
    }

    /**
     * 是否是长图
     */
    private boolean isLongerImage(int imageWidth, int imageHeight) {
        if (imageHeight == 0 || imageWidth == 0) {
            return false;
        }
        return imageHeight / imageWidth >= 3;
    }

    public void setOnPhotoClickListener(OnPhotoClickListener listener) {
        this.mListener = listener;
    }

    @SuppressLint("HandlerLeak")
    private class PreviewHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == HANDLER_LOAD_LONG) {
                if (TextUtils.isEmpty(mPath)) {
                    return;
                }
                Config.imageEngine.loadNetLongImage(getContext(), iv, mPath, new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                });
            }
        }
    }
}
