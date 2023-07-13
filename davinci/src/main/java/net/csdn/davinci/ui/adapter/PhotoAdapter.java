package net.csdn.davinci.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import net.csdn.davinci.Config;
import net.csdn.davinci.R;
import net.csdn.davinci.core.entity.DavinciPhoto;
import net.csdn.davinci.utils.DavinciToastUtils;
import net.csdn.davinci.utils.DensityUtils;
import net.csdn.davinci.utils.ImageShowUtils;
import net.csdn.davinci.utils.SystemUtils;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_CAMERA = 1000;
    private static final int TYPE_PHOTO = 1001;

    private final int mImageWidth;
    private final Context mContext;
    private List<DavinciPhoto> mDatas;

    private final OnPhotoSelectChangeListener mListener;
    private final OnCameraClickListener mOnCameraClickListener;
    private final OnImageClickListener mOnImageClickListener;

    public interface OnPhotoSelectChangeListener {
        void onChange();
    }

    public interface OnCameraClickListener {
        void onClick();
    }

    public interface OnImageClickListener {
        void onClick(DavinciPhoto photo);
    }

    public PhotoAdapter(Context context, OnPhotoSelectChangeListener listener, OnCameraClickListener onCameraClickListener, OnImageClickListener onImageClickListener) {
        this.mContext = context;
        this.mListener = listener;
        this.mOnCameraClickListener = onCameraClickListener;
        this.mOnImageClickListener = onImageClickListener;
        this.mImageWidth = (SystemUtils.getScreenWidth(context) - DensityUtils.dp2px(context, Config.column)) / 4;
    }

    public void setDatas(List<DavinciPhoto> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    public List<DavinciPhoto> getDatas() {
        return mDatas;
    }

    @Override
    public int getItemCount() {
        int count = (mDatas == null || mDatas.size() == 0) ? 0 : mDatas.size();
        if (Config.showCamera) {
            return count + 1;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        return (Config.showCamera && position == 0) ? TYPE_CAMERA : TYPE_PHOTO;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CAMERA) {
            return new CameraHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.davinci_item_photo_camera, parent, false));
        }
        return new PhotoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.davinci_item_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        // 相机
        if (viewHolder instanceof CameraHolder) {
            CameraHolder cameraHolder = (CameraHolder) viewHolder;
            boolean canClick = Config.selectedPhotos.size() < Config.maxSelectable;
            if (!canClick) {
                cameraHolder.viewShadow.setVisibility(View.VISIBLE);
            } else {
                cameraHolder.viewShadow.setVisibility(View.GONE);
            }
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!canClick) {
                        DavinciToastUtils.showToast(mContext, mContext.getResources().getString(R.string.davinci_over_max_count_tips, Config.maxSelectable));
                        return;
                    }
                    if (mOnCameraClickListener != null) {
                        mOnCameraClickListener.onClick();
                    }
                }
            });
            return;
        }
        // 图片
        PhotoHolder holder = (PhotoHolder) viewHolder;
        DavinciPhoto photo;
        if (Config.showCamera) {
            photo = mDatas.get(position - 1);
        } else {
            photo = mDatas.get(position);
        }
        ImageShowUtils.loadThumbnail(mContext, mImageWidth, holder.ivPhoto, photo.uri.toString());
        // 图片选中状态
        boolean isSelected = Config.selectedPhotos.contains(photo);
        holder.rlSelected.setSelected(isSelected);
        holder.tvSelected.setText(isSelected ? Config.selectedPhotos.indexOf(photo) + 1 + "" : "");
        holder.viewShadow.setVisibility(Config.selectedPhotos.size() >= Config.maxSelectable && !isSelected ? View.VISIBLE : View.GONE);

        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnImageClickListener != null) {
                    mOnImageClickListener.onClick(photo);
                }
            }
        });
        holder.rlSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!view.isSelected() && Config.selectedPhotos.size() >= Config.maxSelectable) {
                    return;
                }
                view.setSelected(!view.isSelected());
                if (view.isSelected()) {
                    // 选中图片
                    Config.selectedPhotos.add(photo);
                    if (Config.selectedPhotos.size() < Config.maxSelectable) {
                        holder.tvSelected.setText(view.isSelected() ? Config.selectedPhotos.indexOf(photo) + 1 + "" : "");
                    } else {
                        notifyDataSetChanged();
                    }
                } else {
                    // 取消选中
                    Config.selectedPhotos.remove(photo);
                    notifyDataSetChanged();
                }
                if (mListener != null) {
                    mListener.onChange();
                }
            }
        });
    }


    static class PhotoHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private RelativeLayout rlSelected;
        private TextView tvSelected;
        private View viewShadow;

        PhotoHolder(View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            rlSelected = itemView.findViewById(R.id.rl_selected);
            tvSelected = itemView.findViewById(R.id.tv_selected);
            viewShadow = itemView.findViewById(R.id.view_shadow);
        }
    }

    static class CameraHolder extends RecyclerView.ViewHolder {
        private View viewShadow;

        CameraHolder(View itemView) {
            super(itemView);
            viewShadow = itemView.findViewById(R.id.view_shadow);
        }
    }

}
