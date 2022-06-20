package net.csdn.davinci.ui.adapter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import net.csdn.davinci.BR;
import net.csdn.davinci.BusEvent;
import net.csdn.davinci.R;
import net.csdn.davinci.databinding.ItemPreviewSelectedBinding;
import net.csdn.mvvm_java.ui.adapter.BindingViewModelAdapter;
import net.csdn.davinci.ui.viewmodel.PreviewSelectedItemViewModel;
import net.csdn.mvvm_java.bus.LiveDataBus;

import java.util.List;

public class PreviewSelectedAdapter extends BindingViewModelAdapter<String, ItemPreviewSelectedBinding> {

    private int mSelectedPosition = 0;


    public PreviewSelectedAdapter(List<String> datas) {
        super(R.layout.item_preview_selected, BR.viewmodel, PreviewSelectedItemViewModel.class, datas);
    }

    @Override
    public void onBind(int position, ItemPreviewSelectedBinding dataBinding, String data) {
        PreviewSelectedItemViewModel viewModel = new PreviewSelectedItemViewModel(mSelectedPosition, position, data);
        dataBinding.setVariable(mVariableId, viewModel);
        dataBinding.setOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedItem(null, position);
                LiveDataBus.getInstance().with(BusEvent.Preview.PREVIEW_SELECTED_CLICK, String.class).setValue(data);
                notifyDataSetChanged();
            }
        });
    }

    public void setSelectedItem(RecyclerView rv, int position) {
        if (rv != null && position >= 0) {
            rv.smoothScrollToPosition(position);
        }
        mSelectedPosition = position;
    }
}

