package huce.edu.vn.appdocsach.adapters;

import android.content.Context;
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
import huce.edu.vn.appdocsach.utils.DatetimeUtils;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private final List<SimpleBookModel> books;
    private final OnTouchItem onTouchItem;
    private final ImageLoader imageLoader;

    public BookAdapter(Context context, List<SimpleBookModel> books, OnTouchItem onTouchItem) {
        this.imageLoader = new ImageLoader(context);
        this.books = books;
        this.onTouchItem = onTouchItem;
    }

    public SimpleBookModel getBookByPosition(int pos) {
        return (books == null || books.isEmpty()) ? null : books.get(pos);
    }

    public void setData(List<SimpleBookModel> newData) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new GenericDiffUtilCallback<>(books, newData));
        books.clear();
        books.addAll(newData);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_book_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        SimpleBookModel book = books.get(position);
        holder.tvMainBookItemNameV.setText(book.getTitle());
        holder.tvMainBookItemUpdatedAtV.setText(DatetimeUtils.countTimeCostedUpToNow(book.getLastUpdatedAt()));
        holder.tvMainBookItemAuthorV.setText(book.getAuthor());
        imageLoader.renderWithCache(book.getCoverImage(), holder.ivMainBookItemCoverImage);
    }

    @Override
    public int getItemCount() {
        return books == null ? 0 : books.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvMainBookItemNameV, tvMainBookItemUpdatedAtV, tvMainBookItemAuthorV;
        ImageView ivMainBookItemCoverImage;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMainBookItemNameV = itemView.findViewById(R.id.tvMainBookItemNameV);
            tvMainBookItemAuthorV = itemView.findViewById(R.id.tvMainBookItemAuthorV);
            tvMainBookItemUpdatedAtV = itemView.findViewById(R.id.tvMainBookItemUpdatedAtV);
            ivMainBookItemCoverImage = itemView.findViewById(R.id.ivMainBookItemCoverImage);
            itemView.setOnClickListener(v -> onTouchItem.onClick(getAdapterPosition()));
        }
    }
}
