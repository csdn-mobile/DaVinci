package net.csdn.davinci.ui.fragment;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import net.csdn.davinci.BR;
import net.csdn.davinci.Config;
import net.csdn.davinci.R;
import net.csdn.davinci.core.engine.PhotoViewTarget;
import net.csdn.davinci.core.entity.DavinciMedia;
import net.csdn.davinci.core.entity.DavinciPhoto;
import net.csdn.davinci.core.entity.DavinciVideo;
import net.csdn.davinci.databinding.DavinciFragmentPreviewBinding;
import net.csdn.davinci.ui.viewmodel.PreviewFragmentViewModel;
import net.csdn.davinci.utils.PhotoUtils;
import net.csdn.mvvm_java.ui.fragment.BaseBindingViewModelFragment;

public class PreviewFragment extends BaseBindingViewModelFragment<DavinciFragmentPreviewBinding, PreviewFragmentViewModel> {

    private static final String IMAGE_ITEM = "image_item";
    private static final String VIDEO_ITEM = "video_item";


    public static PreviewFragment newInstance(Object media) {
        PreviewFragment fragment = new PreviewFragment();
        Bundle bundle = new Bundle();
        if (media instanceof DavinciPhoto) {
            bundle.putParcelable(IMAGE_ITEM, (DavinciPhoto) media);
        } else if (media instanceof DavinciVideo) {
            bundle.putParcelable(VIDEO_ITEM, (DavinciVideo) media);
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.davinci_fragment_preview;
    }

    @Override
    public int getVariableId() {
        return BR.viewmodel;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() == null) {
            return;
        }
        try {
            if (getArguments().containsKey(IMAGE_ITEM)) {
                mViewModel.isVideo = false;
                mViewModel.image = getArguments().getParcelable(IMAGE_ITEM);
            } else if (getArguments().containsKey(VIDEO_ITEM)) {
                mViewModel.isVideo = true;
                mViewModel.video = getArguments().getParcelable(VIDEO_ITEM);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mViewModel.isVideo && mViewModel.video == null ||
                !mViewModel.isVideo && mViewModel.image == null) {
            return;
        }

        if (mViewModel.isVideo) {
            mBinding.rlVideo.setVisibility(View.VISIBLE);
            Config.imageEngine.loadLocalImage(getContext(), mBinding.ivCover, mViewModel.video.uri.toString());
            mBinding.videoView.setVideoPath(mViewModel.video.uri.toString());
            mBinding.rlPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBinding.videoView.start();
                    mBinding.rlPlay.setVisibility(View.GONE);
                }
            });
            mBinding.videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBinding.videoView.pause();
                    mBinding.rlPlay.setVisibility(View.VISIBLE);
                }
            });
            mBinding.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mBinding.videoView.pause();
                    mBinding.rlPlay.setVisibility(View.VISIBLE);
                }
            });
        } else {
            mBinding.iv.setVisibility(View.VISIBLE);
            mBinding.ivLong.setVisibility(View.GONE);
            mBinding.rlVideo.setVisibility(View.GONE);
            mBinding.ivLong.setMaxScale(20f);

            if (mViewModel.image.path.startsWith("http")) {
                mBinding.progressBar.setVisibility(View.VISIBLE);
                Config.imageEngine.loadNetImage(getContext(), mBinding.iv, mViewModel.image.path, new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        mBinding.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        // 不要调整顺序！！！！
                        mBinding.iv.setImageDrawable(resource);
                        int height = resource.getBounds().height();
                        int width = resource.getBounds().width();
                        boolean longerImage = mViewModel.isLongerImage(width, height);
                        if (!longerImage) {
                            mBinding.iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            mBinding.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                        // 设置长图缩放
                        mBinding.iv.setVisibility(View.GONE);
                        mBinding.ivLong.setVisibility(View.VISIBLE);
                        Config.imageEngine.loadNetLongImage(getContext(), mViewModel.image.path, new PhotoViewTarget(mBinding.ivLong, mBinding.progressBar));
                        return true;
                    }
                });
            } else {
                mBinding.progressBar.setVisibility(View.GONE);
                Point size = PhotoUtils.getBitmapSize(mViewModel.image.uri, getActivity());
                if (!mViewModel.isLongerImage(size.x, size.y)) {
                    mBinding.iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    Config.imageEngine.loadLocalImage(getContext(), size.x, size.y, mBinding.iv, mViewModel.image.uri.toString());
                } else {
                    mBinding.iv.setVisibility(View.GONE);
                    mBinding.ivLong.setVisibility(View.VISIBLE);
                    mBinding.iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Config.imageEngine.loadLocalLongImage(mBinding.ivLong, mViewModel.image.path);
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mViewModel.isVideo) {
            mBinding.videoView.pause();
            mBinding.rlPlay.setVisibility(View.VISIBLE);
        }
    }
}
