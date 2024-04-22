package huce.edu.vn.appdocsach.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.models.category.CategoryResponse;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private final List<CategoryResponse> categoryModels;

    public CategoryAdapter(List<CategoryResponse> categoryModels) {
        this.categoryModels = categoryModels;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryResponse category = categoryModels.get(position);
        holder.btnCategoryItem.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        Button btnCategoryItem;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            btnCategoryItem = itemView.findViewById(R.id.btnCategoryItem);
        }
    }
}
