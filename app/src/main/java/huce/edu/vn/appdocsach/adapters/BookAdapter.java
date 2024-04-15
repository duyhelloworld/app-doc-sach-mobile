package huce.edu.vn.appdocsach.adapters;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.stream.Collectors;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.models.book.BookResponseModel;
import huce.edu.vn.appdocsach.models.category.SimpleCategoryModel;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private final List<BookResponseModel> books;

    public BookAdapter(@NonNull List<BookResponseModel> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        BookResponseModel book = books.get(position);
        holder.tvItemNameV.setText(book.getTitle());
        holder.tvItemReleaseDateV.setText(book.getReleaseDate().toString());
        holder.tvItemAuthorV.setText(book.getAuthor());
        holder.tvItemCategories.setText(book.getCategories().stream().map(SimpleCategoryModel::getName).collect(Collectors.joining(", ")));

        Picasso.get()
                .load(Uri.parse(book.getCoverImage()))
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.ivItemCoverImage);
    }

    @Override
    public int getItemCount() {

        return books.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemNameV, tvItemReleaseDateV, tvItemAuthorV, tvItemCategories;
        ImageView ivItemCoverImage;
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemNameV = itemView.findViewById(R.id.tvItemNameV);
            tvItemAuthorV = itemView.findViewById(R.id.tvItemAuthorV);
            tvItemReleaseDateV = itemView.findViewById(R.id.tvItemReleaseDateV);
            ivItemCoverImage = itemView.findViewById(R.id.ivItemCoverImage);
            tvItemCategories = itemView.findViewById(R.id.tvItemCategories);
        }
    }
}
