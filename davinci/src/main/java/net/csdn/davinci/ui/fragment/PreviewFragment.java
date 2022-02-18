package net.csdn.davinci.ui.fragment;

import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.csdn.davinci.Config;
import net.csdn.davinci.R;
import net.csdn.davinci.utils.PhotoUtils;

import java.io.File;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

public class PreviewFragment extends Fragment {

    private static final String ARGS_ITEM = "args_item";

    private ImageViewTouch.OnImageViewTouchSingleTapListener mListener;

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
        String path = getArguments().getString(ARGS_ITEM);
        if (path == null) {
            return;
        }

        ImageViewTouch iv = view.findViewById(R.id.iv);
        iv.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        if (mListener != null) {
            iv.setSingleTapListener(mListener);
        }
        Point size = PhotoUtils.getBitmapSize(Uri.fromFile(new File(path)), getActivity());
        Config.imageEngine.loadImage(getContext(), size.x, size.y, iv, path);
    }

    public void setOnImageViewTouchSingleTapListener(ImageViewTouch.OnImageViewTouchSingleTapListener listener) {
        this.mListener = listener;
    }
}
