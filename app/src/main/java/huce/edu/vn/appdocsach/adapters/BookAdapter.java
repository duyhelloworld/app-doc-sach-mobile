package huce.edu.vn.appdocsach.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.callbacks.GenericDiffUtilCallback;
import huce.edu.vn.appdocsach.callbacks.OnLoadMore;
import huce.edu.vn.appdocsach.callbacks.OnTouchItem;
import huce.edu.vn.appdocsach.configurations.ImageLoader;
import huce.edu.vn.appdocsach.models.book.SimpleBookModel;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private final List<SimpleBookModel> books;
    private final OnTouchItem onTouchItem;
    private final ImageLoader imageLoader = ImageLoader.getInstance();
    private boolean isLoading = false;

    public BookAdapter(List<SimpleBookModel> books, RecyclerView recyclerView, OnTouchItem onTouchItem, OnLoadMore onLoadMore) {
        this.books = books;
        this.onTouchItem = onTouchItem;
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        assert layoutManager != null;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                    int totalItemCount = layoutManager.getItemCount();
                    if (!isLoading && lastVisibleItemPosition == totalItemCount - 1) {
                        onLoadMore.loadMore();
                    }
                }
            }
        });
    }

    public void setLoaded() {
        this.isLoading = false;
    }

    public SimpleBookModel getBookByPosition(int pos) {
        return (books.isEmpty()) ? null : books.get(pos);
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
        imageLoader.renderWithCache(book.getCoverImage(), holder.ivMainBookItemCoverImage);
    }

    @Override
    public int getItemCount() {
        return books == null ? 0 : books.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvMainBookItemNameV;
        ImageView ivMainBookItemCoverImage;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMainBookItemNameV = itemView.findViewById(R.id.tvMainBookItemNameV);
            ivMainBookItemCoverImage = itemView.findViewById(R.id.ivMainBookItemCoverImage);
            itemView.setOnClickListener(v -> onTouchItem.onClick(getAdapterPosition()));
        }
    }
}
