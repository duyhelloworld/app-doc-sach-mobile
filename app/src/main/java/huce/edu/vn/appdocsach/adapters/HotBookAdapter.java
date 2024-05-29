package huce.edu.vn.appdocsach.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.callbacks.GenericDiffUtilCallback;
import huce.edu.vn.appdocsach.callbacks.OnTouchItem;
import huce.edu.vn.appdocsach.configurations.ImageLoader;
import huce.edu.vn.appdocsach.models.book.SimpleBookModel;

public class HotBookAdapter extends RecyclerView.Adapter<HotBookAdapter.HotBookViewHolder> {
    private final List<SimpleBookModel> books;
    private final OnTouchItem onTouchItem;
    private final ImageLoader imageLoader = ImageLoader.getInstance();

    public HotBookAdapter(List<SimpleBookModel> books, OnTouchItem onTouchItem) {
        this.books = books;
        this.onTouchItem = onTouchItem;
    }

    public SimpleBookModel getBookByPosition(int pos) {
        return (books.isEmpty()) ? null : books.get(pos);
    }

    @NonNull
    @Override
    public HotBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HotBookViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_hot_book_item, parent, false));
    }

    public void setData(List<SimpleBookModel> newData) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new GenericDiffUtilCallback<>(books, newData));
        books.clear();
        books.addAll(newData);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public void onBindViewHolder(@NonNull HotBookViewHolder holder, int position) {
        SimpleBookModel book = books.get(position);
        holder.tvMainHotBookName.setText(book.getTitle());
        imageLoader.show(book.getCoverImage(), holder.ivMainHotBookCover);
    }

    @Override
    public int getItemCount() {
        return books == null ? 0 : books.size();
    }

    class HotBookViewHolder extends RecyclerView.ViewHolder {

        ImageView ivMainHotBookCover;
        TextView tvMainHotBookName;

        public HotBookViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMainHotBookCover = itemView.findViewById(R.id.ivMainHotBookCover);
            tvMainHotBookName = itemView.findViewById(R.id.tvMainHotBookName);
            itemView.setOnClickListener(v -> onTouchItem.onClick(getAdapterPosition()));
        }
    }
}
