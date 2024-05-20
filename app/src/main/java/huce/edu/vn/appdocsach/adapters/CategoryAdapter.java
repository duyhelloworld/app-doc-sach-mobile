package huce.edu.vn.appdocsach.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.callbacks.GenericDiffUtilCallback;
import huce.edu.vn.appdocsach.callbacks.OnTouchItem;
import huce.edu.vn.appdocsach.models.category.SimpleCategoryModel;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private final List<SimpleCategoryModel> data;
    private final OnTouchItem onTouchItem;

    public CategoryAdapter(List<SimpleCategoryModel> data, OnTouchItem onTouchItem)
    {
        this.data = data;
        this.onTouchItem = onTouchItem;
    }

    public SimpleCategoryModel getData(int pos) {
        return (data == null || data.isEmpty()) ? null : data.get(pos);
    }

    public void setData(List<SimpleCategoryModel> newData) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new GenericDiffUtilCallback<>(data, newData));
        diffResult.dispatchUpdatesTo(this);
        data.clear();
        data.addAll(newData);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_book_detail_category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        SimpleCategoryModel category = data.get(position);
        holder.btnCategoryItem.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        Button btnCategoryItem;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            btnCategoryItem = itemView.findViewById(R.id.btnCategoryItem);
            btnCategoryItem.setOnClickListener(l -> onTouchItem.onClick(getAdapterPosition()));
        }
    }
}
