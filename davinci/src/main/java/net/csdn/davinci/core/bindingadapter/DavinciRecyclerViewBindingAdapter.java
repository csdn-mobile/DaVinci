package net.csdn.davinci.core.bindingadapter;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView的自定义属性
 *
 * @author by KG on 2022/05/16
 */
public class DavinciRecyclerViewBindingAdapter {

    /**
     * 设置Adapter
     */
    @BindingAdapter("davinci_bind_adapter")
    public static void setAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    /**
     * 设置RecyclerView为GridLayoutManager
     */
    @BindingAdapter("davinci_bind_column")
    public static void setColumnCount(RecyclerView recyclerView, int count) {
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), count));
    }
}
