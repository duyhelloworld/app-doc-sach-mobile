package huce.edu.vn.appdocsach.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.callbacks.OnTouchItem;
import huce.edu.vn.appdocsach.models.chapter.OnlyNameChapterModel;
import huce.edu.vn.appdocsach.models.chapter.SimpleChapterModel;
import huce.edu.vn.appdocsach.utils.DatetimeUtils;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {

    private final List<SimpleChapterModel> data;
    private final OnTouchItem onTouchItem;

    public ChapterAdapter(List<SimpleChapterModel> data, OnTouchItem onTouchItem) {
        this.data = data;
        this.onTouchItem = onTouchItem;
    }

    private boolean isAccessible() {
        return data == null || data.isEmpty();
    }

    public SimpleChapterModel getData(int pos) {
        return isAccessible() ? null : data.get(pos);
    }

    public int getFirstItemIndex() {
        return 0;
    }

    public int getLastItemIndex() {
        return data.size() - 1;
    }

    public List<OnlyNameChapterModel> getOnlyNamModel() {
        return isAccessible() ? null : data.stream()
                .map(d -> new OnlyNameChapterModel(d.getId(), d.getTitle()))
                .collect(Collectors.toList());
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_book_detail_chapter_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        SimpleChapterModel model = data.get(position);
        holder.tvBookDetailChapterTitle.setText(model.getTitle());
        holder.tvBookDetailChapterUpdateAtV.setText(DatetimeUtils.countTimeCostedUpToNow(model.getLastUpdatedAt()));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookDetailChapterTitle, tvBookDetailChapterUpdateAtV;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookDetailChapterTitle = itemView.findViewById(R.id.tvBookDetailChapterTitle);
            tvBookDetailChapterUpdateAtV = itemView.findViewById(R.id.tvBookDetailChapterUpdatedAtV);
            itemView.setOnClickListener(l -> onTouchItem.onClick(getAdapterPosition()));
        }
    }
}
