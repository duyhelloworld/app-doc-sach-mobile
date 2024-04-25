package huce.edu.vn.appdocsach.callbacks;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import huce.edu.vn.appdocsach.models.BaseModel;

public class GenericDiffUtilCallback<T extends BaseModel> extends DiffUtil.Callback {
    private final List<T> oldData;
    private final List<T> newData;

    public GenericDiffUtilCallback(List<T> oldData, List<T> newData) {
        this.oldData = oldData;
        this.newData = newData;
    }

    @Override
    public int getOldListSize() {
        return oldData != null ? oldData.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newData != null ? newData.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldData.get(oldItemPosition).getId() == newData.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldData.get(oldItemPosition).compareTo(newData.get(newItemPosition));
    }
}
