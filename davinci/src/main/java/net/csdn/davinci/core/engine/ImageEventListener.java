package net.csdn.davinci.core.engine;

import android.graphics.PointF;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

public class ImageEventListener implements SubsamplingScaleImageView.OnImageEventListener{

    private SubsamplingScaleImageView view;

    public ImageEventListener(SubsamplingScaleImageView view) {
        this.view = view;
    }

    @Override
    public void onReady() {

    }

    @Override
    public void onImageLoaded() {
        if (view.getSWidth() != 0
                && view.getSHeight() != 0) {
            if (view.getSHeight() / view.getSWidth() > 2) {
                view.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_START);
                view.setScaleAndCenter(view.getScale(), new PointF(0, 0));
            }
        }
    }

    @Override
    public void onPreviewLoadError(Exception e) {

    }

    @Override
    public void onImageLoadError(Exception e) {

    }

    @Override
    public void onTileLoadError(Exception e) {

    }

    @Override
    public void onPreviewReleased() {

    }
}
