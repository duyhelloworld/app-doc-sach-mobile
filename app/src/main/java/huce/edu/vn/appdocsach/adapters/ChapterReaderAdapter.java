package huce.edu.vn.appdocsach.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.callbacks.OnHoldEvent;
import huce.edu.vn.appdocsach.callbacks.RenderImageDiffUtilCallback;
import huce.edu.vn.appdocsach.configurations.ImageLoader;

public class ChapterReaderAdapter extends RecyclerView.Adapter<ChapterReaderAdapter.ChapterReaderViewHolder> {
    private final List<String> urls;
    private final ImageLoader imageLoader = ImageLoader.getInstance();
    private final OnHoldEvent onHoldEvent;

    public ChapterReaderAdapter(List<String> urls, OnHoldEvent onHoldEvent) {
        this.urls = urls;
        this.onHoldEvent = onHoldEvent;
    }

    public void setData(List<String> newUrls) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new RenderImageDiffUtilCallback(urls, newUrls));
        urls.clear();
        urls.addAll(newUrls);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ChapterReaderAdapter.ChapterReaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChapterReaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_chapter_reader_image_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterReaderAdapter.ChapterReaderViewHolder holder, int position) {
        imageLoader.show(urls.get(position), holder.ivChapterReaderImage);
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    class ChapterReaderViewHolder extends RecyclerView.ViewHolder {
        ImageView ivChapterReaderImage;

        public ChapterReaderViewHolder(@NonNull View itemView) {
            super(itemView);
            ivChapterReaderImage = itemView.findViewById(R.id.ivChapterReaderImage);
            itemView.setOnLongClickListener(l -> {
                onHoldEvent.onHold();
                return true;
            });
        }
    }

    //    Chống crash ảnh
    @Override
    public void onViewDetachedFromWindow(@NonNull ChapterReaderViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        imageLoader.cancelRequest(holder.ivChapterReaderImage);
    }
}
