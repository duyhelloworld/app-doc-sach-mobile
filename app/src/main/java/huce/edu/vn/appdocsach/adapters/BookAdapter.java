package huce.edu.vn.appdocsach.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.models.book.BookResponseModel;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private final List<BookResponseModel> books;

    public BookAdapter(@NonNull List<BookResponseModel> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_book, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        BookResponseModel book = books.get(position);
        holder.tvFrmBookTitle.setText(book.getTitle());
        holder.tvFrmBookReleaseDate.setText(book.getReleaseDate().toString());

        Picasso.get()
                .load(book.getCoverImage())
                .into(holder.ivFrmBookCoverImage);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView tvFrmBookTitle, tvFrmBookReleaseDate;
        ImageView ivFrmBookCoverImage;

        public BookViewHolder(@NonNull View view) {
            super(view);
            tvFrmBookTitle = view.findViewById(R.id.tvFrmBookTitle);
            tvFrmBookReleaseDate = view.findViewById(R.id.tvFrmBookReleaseDate);
            ivFrmBookCoverImage = view.findViewById(R.id.ivFrmBookCoverImage);
        }
    }
}
