package huce.edu.vn.appdocsach.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.callbacks.OnTouchItem;
import huce.edu.vn.appdocsach.configurations.ImageLoader;
import huce.edu.vn.appdocsach.models.book.SimpleBookResponseModel;
import huce.edu.vn.appdocsach.utils.DatetimeUtils;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private final List<SimpleBookResponseModel> books;
    private final OnTouchItem onTouchItem;

    public BookAdapter(List<SimpleBookResponseModel> books, OnTouchItem onTouchItem) {
        this.books = books;
        this.onTouchItem = onTouchItem;
    }

    public SimpleBookResponseModel getBookByPosition(int pos) {

        return (books == null || books.isEmpty()) ? null : books.get(pos);
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_book_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        SimpleBookResponseModel book = books.get(position);
        holder.tvMainBookItemNameV.setText(book.getTitle());
        holder.tvMainBookItemUpdatedAtV.setText(DatetimeUtils.countTimeCostedUpToNow(book.getLastUpdatedAt()));
        holder.tvMainBookItemAuthorV.setText(book.getAuthor());
        ImageLoader.renderWithCache(book.getCoverImage(), holder.ivMainBookItemCoverImage);
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
