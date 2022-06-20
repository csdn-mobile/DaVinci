package net.csdn.davinci.ui.fragment;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import net.csdn.davinci.Config;
import net.csdn.davinci.R;
import net.csdn.davinci.core.engine.PhotoViewTarget;
import net.csdn.davinci.databinding.FragmentPreviewBinding;
import net.csdn.davinci.ui.viewmodel.PreviewFragmentViewModel;
import net.csdn.davinci.utils.PhotoUtils;
import net.csdn.mvvm_java.ui.fragment.BaseBindingViewModelFragment;

import java.io.File;

public class PreviewFragment extends BaseBindingViewModelFragment<FragmentPreviewBinding, PreviewFragmentViewModel> {

    private static final String ARGS_ITEM = "args_item";

    public static PreviewFragment newInstance(String path) {
        PreviewFragment fragment = new PreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_ITEM, path);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_preview;
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
        mViewModel.setPath(getArguments().getString(ARGS_ITEM));
        if (mViewModel.getPath() == null) {
            return;
        }

        mBinding.iv.setVisibility(View.VISIBLE);
        mBinding.ivLong.setVisibility(View.GONE);
        mBinding.ivLong.setMaxScale(20f);

        if (mViewModel.getPath().startsWith("http")) {
            mBinding.progressBar.setVisibility(View.VISIBLE);
            Config.imageEngine.loadNetImage(getContext(), mBinding.iv, mViewModel.getPath(), new RequestListener<Drawable>() {
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
                    Config.imageEngine.loadNetLongImage(getContext(), mViewModel.getPath(), new PhotoViewTarget(mBinding.ivLong, mBinding.progressBar));
                    return true;
                }
            });
        } else {
            mBinding.progressBar.setVisibility(View.GONE);
            Point originSize = PhotoUtils.getOriginSize(mViewModel.getPath(), getActivity());
            if (!mViewModel.isLongerImage(originSize.x, originSize.y)) {
                Point size = PhotoUtils.getBitmapSize(Uri.fromFile(new File(mViewModel.getPath())), getActivity());
                mBinding.iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Config.imageEngine.loadLocalImage(getContext(), size.x, size.y, mBinding.iv, mViewModel.getPath());
            } else {
                mBinding.iv.setVisibility(View.GONE);
                mBinding.ivLong.setVisibility(View.VISIBLE);
                mBinding.iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Config.imageEngine.loadLocalLongImage(mBinding.ivLong, mViewModel.getPath());
            }
        }
    }
}
