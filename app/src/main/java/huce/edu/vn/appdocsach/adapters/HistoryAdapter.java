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

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private final OnTouchItem onTouchItem;
    private final List<String> historySet;

    public HistoryAdapter(List<String> historySet, OnTouchItem onTouchItem) {
        this.onTouchItem = onTouchItem;
        this.historySet = historySet;
    }

    public String getHistory(int pos) {
        return historySet.get(pos);
    }

    public void clear() {
        int count = getItemCount();
        historySet.clear();
        notifyItemRangeRemoved(0, count - 1);
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_book_search_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        String history = historySet.get(position);
        holder.tvBookSearchQuery.setText(history);
    }

    @Override
    public int getItemCount() {
        return historySet == null ? 0 : historySet.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookSearchQuery;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookSearchQuery = itemView.findViewById(R.id.tvBookSearchQuery);
            tvBookSearchQuery.setOnClickListener(v -> onTouchItem.onClick(getAdapterPosition()));
        }
    }
}
