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
import net.csdn.davinci.core.entity.DavinciVideo;
import net.csdn.davinci.utils.DensityUtils;
import net.csdn.davinci.utils.ImageShowUtils;
import net.csdn.davinci.utils.SystemUtils;
import net.csdn.davinci.utils.TimeUtils;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {

    private static final int MAX_VIDEO_COUNT = 1;

    private final Context mContext;
    private List<DavinciVideo> mDatas;
    private final int mImageWidth;

    private OnVideoSelectChangeListener mListener;
    private OnVideoClickListener mOnVideoClickListener;

    public interface OnVideoSelectChangeListener {
        void onChange();
    }

    public interface OnVideoClickListener {
        void onClick(DavinciVideo video);
    }

    public VideoAdapter(Context context, OnVideoSelectChangeListener listener, OnVideoClickListener onVideoClickListener) {
        this.mContext = context;
        this.mListener = listener;
        this.mOnVideoClickListener = onVideoClickListener;
        this.mImageWidth = (SystemUtils.getScreenWidth(context) - DensityUtils.dp2px(context, Config.column)) / 4;
    }

    public void setDatas(List<DavinciVideo> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    public List<DavinciVideo> getDatas() {
        return mDatas;
    }

    @Override
    public int getItemCount() {
        return (mDatas == null || mDatas.size() == 0) ? 0 : mDatas.size();
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false));
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        DavinciVideo video = mDatas.get(position);
        String uriPath = video.uri.toString();
        ImageShowUtils.loadThumbnail(mContext, mImageWidth, holder.ivPhoto, uriPath);

        // 视频选中状态
        boolean isSelected = Config.selectedVideos.contains(video);
        holder.rlSelected.setSelected(isSelected);
        holder.tvSelected.setText(isSelected ? Config.selectedVideos.indexOf(video) + 1 + "" : "");
        holder.viewShadow.setVisibility(Config.selectedVideos.size() >= MAX_VIDEO_COUNT && !isSelected ? View.VISIBLE : View.GONE);
        holder.tvTime.setText(TimeUtils.formatMillisecond(video.duration));

        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnVideoClickListener != null) {
                    mOnVideoClickListener.onClick(video);
                }
            }
        });
        holder.rlSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!view.isSelected() && Config.selectedVideos.size() >= MAX_VIDEO_COUNT) {
                    return;
                }
                view.setSelected(!view.isSelected());
                if (view.isSelected()) {
                    // 选中视频
                    Config.selectedVideos.add(video);
                    if (Config.selectedVideos.size() < MAX_VIDEO_COUNT) {
                        holder.tvSelected.setText(view.isSelected() ? Config.selectedVideos.indexOf(video) + 1 + "" : "");
                    } else {
                        notifyDataSetChanged();
                    }
                } else {
                    // 取消选中
                    Config.selectedVideos.remove(video);
                    notifyDataSetChanged();
                }
                if (mListener != null) {
                    mListener.onChange();
                }
            }
        });
    }


    static class VideoHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private RelativeLayout rlSelected;
        private TextView tvSelected;
        private TextView tvTime;
        private View viewShadow;

        VideoHolder(View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            rlSelected = itemView.findViewById(R.id.rl_selected);
            tvSelected = itemView.findViewById(R.id.tv_selected);
            tvTime = itemView.findViewById(R.id.tv_time);
            viewShadow = itemView.findViewById(R.id.view_shadow);
        }
    }
}
