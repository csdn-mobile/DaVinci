package net.csdn.davinci.core.engine;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;

public class PhotoViewTarget extends CustomViewTarget<SubsamplingScaleImageView, File> {

    private ProgressBar progressBar;

    public PhotoViewTarget(@NonNull SubsamplingScaleImageView view) {
        this(view, null);
    }

    public PhotoViewTarget(@NonNull SubsamplingScaleImageView view, ProgressBar progressBar) {
        super(view);
        this.progressBar = progressBar;
    }

    @Override
    protected void onResourceCleared(@Nullable Drawable placeholder) {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadFailed(@Nullable Drawable errorDrawable) {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        view.setOnImageEventListener(new ImageEventListener(view));
        view.setImage(ImageSource.uri(resource.getAbsolutePath()));
    }
}
