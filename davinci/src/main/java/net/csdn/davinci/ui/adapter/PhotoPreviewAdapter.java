package net.csdn.davinci.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.csdn.davinci.Config;
import net.csdn.davinci.DaVinci;
import net.csdn.davinci.R;
import net.csdn.davinci.utils.DensityUtils;

import java.util.List;

public class PhotoPreviewAdapter extends RecyclerView.Adapter<PhotoPreviewAdapter.PhotoPreviewHolder> {

    private Context mContext;
    private List<String> mDatas;
    private int mImageWidth;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener {
        void onDelete(int position);
    }

    public PhotoPreviewAdapter(Context context, OnDeleteClickListener onDeleteClickListener) {
        this.mContext = context;
        this.mImageWidth = DensityUtils.dp2px(context, 80);
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public void setDatas(List<String> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    public List<String> getDatas() {
        return mDatas;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public PhotoPreviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoPreviewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_preview, parent, false));
    }

    @Override
    public void onBindViewHolder(PhotoPreviewHolder holder, int position) {
        String uriPath = mDatas.get(position);
        Config.imageEngine.loadThumbnail(mContext, mImageWidth, R.color.davinci_place_holder, holder.ivPhoto, uriPath);
        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DaVinci.preview(false)
                        .previewSelectable(true)
                        .start((Activity) mContext, uriPath);
            }
        });
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

        PhotoPreviewHolder(View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            rlDelete = itemView.findViewById(R.id.rl_delete);
        }
    }

}
