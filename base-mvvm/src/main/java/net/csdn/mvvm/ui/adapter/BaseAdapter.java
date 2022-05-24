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
public abstract class BaseAdapter<T, DB extends ViewDataBinding> extends RecyclerView.Adapter<BaseAdapter.BindingHolder<DB>> implements AdapterMethod<T> {

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
        onBind(position, holder.mBinding, mDatas.get(position));
        holder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public List<T> getDatas() {
        return mDatas;
    }

    @Override
    public void setDatas(List<T> datas) {
        this.mDatas = datas;
        createDatas();
        notifyDataSetChanged();
    }

    @Override
    public void addDatas(List<T> datas) {
        if (datas == null || datas.size() <= 0) {
            return;
        }
        createDatas();
        mDatas.addAll(datas);
        notifyItemRangeInserted(mDatas.size() - datas.size(), datas.size());
    }

    @Override
    public void addDatas(int position, List<T> datas) {
        if (datas == null || datas.size() <= 0) {
            return;
        }
        createDatas();
        mDatas.addAll(position, datas);
        notifyItemRangeInserted(position, datas.size());
    }

    @Override
    public void addData(T data) {
        if (data == null) {
            return;
        }
        createDatas();
        mDatas.add(data);
        notifyItemInserted(mDatas.size() - 1);
    }

    @Override
    public void addData(int position, T data) {
        if (data == null) {
            return;
        }
        createDatas();
        mDatas.add(position, data);
        notifyItemInserted(position);
    }

    @Override
    public void removeData(int position) {
        if (mDatas == null || mDatas.size() <= 0) {
            return;
        }
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    private void createDatas() {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
    }

    public abstract void onBind(int position, DB dataBinding, T data);

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
