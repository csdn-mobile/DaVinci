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
import net.csdn.davinci.core.entity.Photo;
import net.csdn.davinci.core.entity.Video;
import net.csdn.davinci.utils.DensityUtils;
import net.csdn.davinci.utils.SystemUtils;
import net.csdn.davinci.utils.TimeUtils;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {

    private static final int MAX_VIDEO_COUNT = 1;

    private final Context mContext;
    private List<Video> mDatas;
    private final int mImageWidth;

    private OnVideoSelectChangeListener mListener;

    public interface OnVideoSelectChangeListener {
        void onChange();
    }

    public VideoAdapter(Context context, OnVideoSelectChangeListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.mImageWidth = (SystemUtils.getScreenWidth(context) - DensityUtils.dp2px(context, Config.column)) / 4;
    }

    public void setDatas(List<Video> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    public List<Video> getDatas() {
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
        Video video = mDatas.get(position);
        String uriPath = video.uri.toString();
        Config.imageEngine.loadThumbnail(mContext, mImageWidth, R.color.davinci_place_holder, holder.ivPhoto, uriPath);

        // 图片选中状态
        boolean isSelected = Config.selectedVideos.contains(uriPath);
        holder.rlSelected.setSelected(isSelected);
        holder.tvSelected.setText(isSelected ? Config.selectedVideos.indexOf(uriPath) + 1 + "" : "");
        holder.viewShadow.setVisibility(Config.selectedVideos.size() >= MAX_VIDEO_COUNT && !isSelected ? View.VISIBLE : View.GONE);
        holder.tvTime.setText(TimeUtils.formatMillisecond(video.duration));

        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DaVinci.preview(false)
                        .previewSelectable(true)
                        .start((Activity) mContext, uriPath);
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
                    // 选中图片
                    Config.selectedVideos.add(uriPath);
                    if (Config.selectedVideos.size() < MAX_VIDEO_COUNT) {
                        holder.tvSelected.setText(view.isSelected() ? Config.selectedVideos.indexOf(uriPath) + 1 + "" : "");
                    } else {
                        notifyDataSetChanged();
                    }
                } else {
                    // 取消选中
                    Config.selectedVideos.remove(uriPath);
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
