package huce.edu.vn.appdocsach.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.callbacks.OnTouchItem;
import huce.edu.vn.appdocsach.models.chapter.SimpleChapterResponseModel;
import huce.edu.vn.appdocsach.utils.DatetimeUtils;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {

    private final List<SimpleChapterResponseModel> data;
    private final OnTouchItem onTouchItem;

    public ChapterAdapter(List<SimpleChapterResponseModel> data, OnTouchItem onTouchItem) {
        this.data = data;
        this.onTouchItem = onTouchItem;
    }

    public SimpleChapterResponseModel getData(int pos) {
        return (data == null || data.isEmpty()) ? null : data.get(pos);
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_book_detail_chapter_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        SimpleChapterResponseModel model = data.get(position);
        holder.tvBookDetailChapterTitle.setText(model.getTitle());
        holder.tvBookDetailChapterUpdateAtV.setText(DatetimeUtils.countTimeCostedUpToNow(model.getLastUpdatedAt()));
    }

    @Override
    public int getItemCount() {
        return 0;
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
