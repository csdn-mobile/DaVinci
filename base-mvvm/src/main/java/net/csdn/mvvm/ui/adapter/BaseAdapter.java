package net.csdn.mvvm.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by KG on 2022/05/19
 */
public abstract class BaseAdapter<T, DB extends ViewDataBinding> extends RecyclerView.Adapter<BaseAdapter.BindingHolder<DB>> {

    protected final int mLayoutId;
    protected final int mVariableId;
    protected List<T> mDatas;

    /**
     * 构造函数
     *
     * @param layoutId   item布局ID
     * @param variableId DataBinding数据源ID
     * @param datas      数据
     */
    protected BaseAdapter(@LayoutRes int layoutId, int variableId, List<T> datas) {
        this.mLayoutId = layoutId;
        this.mVariableId = variableId;
        this.mDatas = datas;
        if (this.mDatas == null) {
            this.mDatas = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public BindingHolder<DB> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DB binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), mLayoutId, parent, false);
        return new BindingHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingHolder<DB> holder, int position) {
        if (position >= mDatas.size()) {
            return;
        }
        onBind(holder.mBinding, mDatas.get(position));
        holder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public abstract void onBind(DB dataBinding, T data);

    /**
     * 弱化Holder，仅存放DataBinding，内部存放ViewModel或者Bean
     */
    public static class BindingHolder<DB extends ViewDataBinding> extends RecyclerView.ViewHolder {

        public final DB mBinding;

        public BindingHolder(@NonNull DB binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }
    }
}
