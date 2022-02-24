package net.csdn.davinci.ui.fragment;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import net.csdn.davinci.utils.PhotoUtils;

import java.io.File;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

public class PreviewFragment extends Fragment {

    private static final String ARGS_ITEM = "args_item";
    private static final String SAVE_PATH = "save_path";

    private ImageViewTouch.OnImageViewTouchSingleTapListener mListener;
    private String mPath;

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

        ImageViewTouch iv = view.findViewById(R.id.iv);
        iv.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        if (mListener != null) {
            iv.setSingleTapListener(mListener);
        }
        if (mPath.startsWith("http")) {
            ProgressBar progressBar = view.findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);
            Config.imageEngine.loadNetImage(getContext(), iv, mPath, new RequestListener<Drawable>() {
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
        } else {
            Point size = PhotoUtils.getBitmapSize(Uri.fromFile(new File(mPath)), getActivity());
            Config.imageEngine.loadImage(getContext(), size.x, size.y, iv, mPath);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!TextUtils.isEmpty(mPath)) {
            outState.putString(SAVE_PATH, mPath);
        }
    }

    public void setOnImageViewTouchSingleTapListener(ImageViewTouch.OnImageViewTouchSingleTapListener listener) {
        this.mListener = listener;
    }

}
