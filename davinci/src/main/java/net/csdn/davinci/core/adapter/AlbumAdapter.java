package net.csdn.davinci.core.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import net.csdn.davinci.R;
import net.csdn.davinci.core.album.AlbumClickListener;
import net.csdn.davinci.core.bean.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ListHolder> {

    private List<Album> mDatas;
    private AlbumClickListener mOnClickListener;

    public AlbumAdapter(List<Album> albums, AlbumClickListener onClickListener) {
        this.mDatas = albums;
        this.mOnClickListener = onClickListener;
        if (this.mDatas == null) {
            this.mDatas = new ArrayList<>();
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListHolder holder, final int position) {
        if (mDatas == null || position >= mDatas.size()) {
            return;
        }
        Album album = mDatas.get(position);
        final RequestOptions options = new RequestOptions()
                .dontAnimate()
                .dontTransform()
                .override(800, 800);
        Glide.with(holder.itemView.getContext())
                .setDefaultRequestOptions(options)
                .load(album.coverPath)
                .thumbnail(0.1f)
                .into(holder.ivCover);
        holder.tvName.setText(album.name);
        holder.tvCount.setText(holder.tvCount.getContext().getString(R.string.davinci_image_count, album.photoList == null ? 0 : album.photoList.size()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener == null) {
                    return;
                }
                mOnClickListener.onAlbumClick(album);
            }
        });
    }

    static class ListHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvName;
        TextView tvCount;

        ListHolder(View rootView) {
            super(rootView);
            ivCover = rootView.findViewById(R.id.iv_dir_cover);
            tvName = rootView.findViewById(R.id.tv_dir_name);
            tvCount = rootView.findViewById(R.id.tv_dir_count);
        }
    }
}

