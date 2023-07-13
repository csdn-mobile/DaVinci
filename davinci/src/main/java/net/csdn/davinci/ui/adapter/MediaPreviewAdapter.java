package net.csdn.davinci.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import net.csdn.davinci.Config;
import net.csdn.davinci.DaVinci;
import net.csdn.davinci.R;
import net.csdn.davinci.core.entity.DavinciMedia;
import net.csdn.davinci.core.entity.DavinciVideo;
import net.csdn.davinci.utils.DensityUtils;
import net.csdn.davinci.utils.ImageShowUtils;
import net.csdn.davinci.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class MediaPreviewAdapter<T extends DavinciMedia> extends RecyclerView.Adapter<MediaPreviewAdapter.PhotoPreviewHolder> {

    private final int mImageWidth;

    private List<T> mDatas;
    private final Context mContext;
    private final OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener {
        void onDelete(int position);
    }

    public MediaPreviewAdapter(Context context, OnDeleteClickListener onDeleteClickListener) {
        this.mContext = context;
        this.mImageWidth = DensityUtils.dp2px(context, 80);
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public void setDatas(List<T> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return mDatas;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public PhotoPreviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoPreviewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.davinci_item_photo_preview, parent, false));
    }

    @Override
    public void onBindViewHolder(PhotoPreviewHolder holder, int position) {
        T media = mDatas.get(position);

        if (media instanceof DavinciVideo) {
            holder.tvTime.setVisibility(View.VISIBLE);
            holder.tvTime.setText(TimeUtils.formatMillisecond(((DavinciVideo) media).duration));
            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 更新预览视频数据
                    ArrayList<DavinciMedia> previes = new ArrayList<>();
                    previes.add(media);
                    Config.previewMedias = previes;
                    // 打开预览
                    DaVinci.preview(false)
                            .previewSelectable(true)
                            .start((Activity) mContext, media);
                }
            });
        } else {
            holder.tvTime.setVisibility(View.GONE);
            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 更新预览数据
                    Config.previewMedias = new ArrayList<>(mDatas);
                    // 打开预览
                    DaVinci.preview(false)
                            .previewSelectable(true)
                            .start((Activity) mContext, media);
                }
            });
        }
        ImageShowUtils.loadThumbnail(mContext, mImageWidth, holder.ivPhoto, media.uri.toString());
        holder.rlDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDelete(holder.getBindingAdapterPosition());
                }
            }
        });
    }


    static class PhotoPreviewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private RelativeLayout rlDelete;
        private TextView tvTime;

        PhotoPreviewHolder(View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            rlDelete = itemView.findViewById(R.id.rl_delete);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }

}
