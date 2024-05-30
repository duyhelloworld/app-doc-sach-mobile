package huce.edu.vn.appdocsach.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.callbacks.GenericDiffUtilCallback;
import huce.edu.vn.appdocsach.callbacks.OnTouchItem;
import huce.edu.vn.appdocsach.models.category.SimpleCategoryModel;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<SimpleCategoryModel> data;
    private static OnTouchItem onTouchItem = null;
    private boolean isExpanded = false;
    private int selectedPosition = -1;

    private static final int VIEW_TYPE_ITEM = 0;
    public static final int VIEW_TYPE_EXPAND_BUTTON = 1;
    private static final int VISIBLE_THRESHOLD = 3; // Number of items to display before showing the expand button

    public CategoryAdapter(List<SimpleCategoryModel> data, OnTouchItem onTouchItem) {
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

    @Override
    public int getItemViewType(int position) {
        if (!isExpanded && position == VISIBLE_THRESHOLD) {
            return VIEW_TYPE_EXPAND_BUTTON;
        }
        return VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false));
        } else {
            return new ExpandButtonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand_button, parent, false));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CategoryViewHolder) {
            SimpleCategoryModel category = data.get(position);
            CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
            categoryViewHolder.txtCateItem.setText(category.getName());

            // Đặt màu nền và màu chữ tùy thuộc vào việc item có được chọn hay không
            int backgroundColor = (selectedPosition == holder.getAdapterPosition()) ? Color.parseColor("#A020F0") : Color.WHITE;
            int textColor = (selectedPosition == holder.getAdapterPosition()) ? Color.WHITE : Color.BLACK;
            categoryViewHolder.itemView.setBackgroundColor(backgroundColor);
            categoryViewHolder.txtCateItem.setTextColor(textColor);

            categoryViewHolder.itemView.setOnClickListener(v -> {
                if (selectedPosition == holder.getAdapterPosition()) {
                    selectedPosition = -1; // Bỏ chọn nếu đã được chọn trước đó
                } else {
                    selectedPosition = holder.getAdapterPosition(); // Chọn item mới
                }
                notifyItemChanged(selectedPosition); // Cập nhật chỉ item được chọn
            });
        } else if (holder instanceof ExpandButtonViewHolder) {
            ((ExpandButtonViewHolder) holder).btnExpand.setOnClickListener(v -> {
                isExpanded = true;
                notifyDataSetChanged();
            });
        }
    }

    @Override
    public int getItemCount() {
        if (isExpanded) {
            return data.size();
        } else {
            // Show VISIBLE_THRESHOLD items + 1 for expand button
            return Math.min(data.size(), VISIBLE_THRESHOLD + 1);
        }
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView txtCateItem;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCateItem = itemView.findViewById(R.id.txtCateItem);
            txtCateItem.setOnClickListener(l -> onTouchItem.onClick(getAdapterPosition()));
        }

    }

    static class ExpandButtonViewHolder extends RecyclerView.ViewHolder {
        Button btnExpand;

        public ExpandButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            btnExpand = itemView.findViewById(R.id.btnExpand);
        }
    }
}
