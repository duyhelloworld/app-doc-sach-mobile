package huce.edu.vn.appdocsach.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.stream.Collectors;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.configurations.ImageLoader;
import huce.edu.vn.appdocsach.models.book.BookResponseModel;
import huce.edu.vn.appdocsach.utils.DatetimeUtils;
import huce.edu.vn.appdocsach.utils.ModelConverter;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private final List<BookResponseModel> books;

    public BookAdapter(@NonNull List<BookResponseModel> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_book_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        BookResponseModel book = books.get(position);
        holder.tvItemNameV.setText(book.getTitle());
        holder.tvItemUpdatedAtV.setText(DatetimeUtils.countTimeCostedUpToNow(book.getLastUpdatedAt()));
        holder.tvItemAuthorV.setText(book.getAuthor());
        CategoryAdapter categoryAdapter = new CategoryAdapter(
                book.getCategories().stream().map(ModelConverter::convert)
                        .collect(Collectors.toList()));
        holder.rvMainCategories.setAdapter(categoryAdapter);
        ImageLoader.renderWithCache(book.getCoverImage(), holder.ivItemCoverImage);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemNameV, tvItemUpdatedAtV, tvItemAuthorV;
        ImageView ivItemCoverImage;
        RecyclerView rvMainCategories;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemNameV = itemView.findViewById(R.id.tvItemNameV);
            tvItemAuthorV = itemView.findViewById(R.id.tvItemAuthorV);
            tvItemUpdatedAtV = itemView.findViewById(R.id.tvItemUpdatedAtV);
            ivItemCoverImage = itemView.findViewById(R.id.ivItemCoverImage);
            rvMainCategories = itemView.findViewById(R.id.rvMainCategories);
        }
    }
}
