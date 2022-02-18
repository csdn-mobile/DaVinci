package net.csdn.davinci.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.csdn.davinci.Config;
import net.csdn.davinci.R;
import net.csdn.davinci.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

public class PreviewSelectedAdapter extends RecyclerView.Adapter<PreviewSelectedAdapter.ListHolder> {

    private int mSelectedPosition = 0;
    private List<String> mDatas;
    private OnSelectedPhotoClickListener mOnClickListener;

    public interface OnSelectedPhotoClickListener {
        void onClick(String path);
    }

    public PreviewSelectedAdapter(List<String> datas) {
        this.mDatas = datas;
        if (this.mDatas == null) {
            this.mDatas = new ArrayList<>();
        }
    }

    public void setDatas(List<String> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    public void setOnSelectedPhotoClickListener(OnSelectedPhotoClickListener onClickListener){
        this.mOnClickListener = onClickListener;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preview_selected, parent, false);
        return new ListHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int position) {
        if (mDatas == null || position >= mDatas.size()) {
            return;
        }
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        layoutParams.leftMargin = position == 0 ? DensityUtils.dp2px(holder.itemView.getContext(), 14) : 0;
        holder.itemView.setLayoutParams(layoutParams);

        String path = mDatas.get(position);

        Config.imageEngine.loadThumbnail(holder.itemView.getContext(), DensityUtils.dp2px(holder.itemView.getContext(), 84), R.color.davinci_black, holder.ivPhoto, path);
        holder.itemView.setSelected(mSelectedPosition == position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedItem(null, position);
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(path);
                }
                notifyDataSetChanged();
            }
        });
    }

    static class ListHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;

        ListHolder(View rootView) {
            super(rootView);
            ivPhoto = rootView.findViewById(R.id.iv_photo);
        }
    }

    public void setSelectedItem(RecyclerView rv, int position) {
        if (rv != null && position >= 0) {
            rv.smoothScrollToPosition(position);
        }
        mSelectedPosition = position;
    }
}

