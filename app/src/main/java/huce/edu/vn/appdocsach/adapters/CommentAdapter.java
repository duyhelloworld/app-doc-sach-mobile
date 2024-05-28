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
import huce.edu.vn.appdocsach.callbacks.OnTouchViewItem;
import huce.edu.vn.appdocsach.configurations.ImageLoader;
import huce.edu.vn.appdocsach.models.comment.SimpleCommentModel;
import huce.edu.vn.appdocsach.utils.DatetimeUtils;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private final OnTouchViewItem onTouchViewItem;
    private final List<SimpleCommentModel> commentModels;
    private final ImageLoader imageLoader = ImageLoader.getInstance();
    private boolean isLoading = false;

    public CommentAdapter(List<SimpleCommentModel> commentModels, RecyclerView recyclerView, OnLoadMore onLoadMore, OnTouchViewItem onTouchViewItem) {
        this.onTouchViewItem = onTouchViewItem;
        this.commentModels = commentModels;
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
        isLoading = false;
    }

    public void add(List<SimpleCommentModel> commentModels) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new GenericDiffUtilCallback<>(this.commentModels, commentModels));
        this.commentModels.clear();
        this.commentModels.addAll(commentModels);
        diffResult.dispatchUpdatesTo(this);
    }

    public void append(List<SimpleCommentModel> commentModels) {
        int oldSize = commentModels.size();
        this.commentModels.addAll(commentModels);
        notifyItemRangeInserted(oldSize, getItemCount());
    }

    public void append(SimpleCommentModel commentModel) {
        this.commentModels.add(commentModel);
        notifyItemInserted(getItemCount() - 1);
    }

    public Integer getCommentId(int pos) {
        return commentModels.get(pos).getId();
    }

    public SimpleCommentModel getComment(int pos) {
        return commentModels.get(pos);
    }

    public void remove(int pos) {
        commentModels.remove(pos);
        notifyItemRemoved(pos);
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_comment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder commentHolder, int position) {
        SimpleCommentModel comment = commentModels.get(position);
        commentHolder.tvCommentItemFullname.setText(comment.getUsername());
        commentHolder.tvCommentItemLastUpdatedAtV.setText(DatetimeUtils.countTimeCostedUpToNow(comment.getCommentAt()));

        if (!comment.getEdited()) {
            commentHolder.tvCommentItemIsEdited.setText("");
        } else {
            commentHolder.tvCommentItemIsEdited.setText(R.string.is_edited);
        }
        commentHolder.tvCommentItemContent.setText(comment.getContent());
        imageLoader.showWithoutCache(comment.getUserAvatar(), commentHolder.ivCommentItemAvatar);
    }

    @Override
    public int getItemCount() {
        return commentModels == null ? 0 : commentModels.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder {
        TextView tvCommentItemFullname, tvCommentItemContent, tvCommentItemIsEdited, tvCommentItemLastUpdatedAtV;
        ImageView ivCommentItemAvatar;
        public CommentHolder(@NonNull View itemView) {

            super(itemView);
            tvCommentItemFullname = itemView.findViewById(R.id.tvCommentItemFullname);
            tvCommentItemContent = itemView.findViewById(R.id.tvCommentItemContent);
            tvCommentItemIsEdited = itemView.findViewById(R.id.tvCommentItemIsEdited);
            tvCommentItemLastUpdatedAtV = itemView.findViewById(R.id.tvCommentItemLastUpdatedAtV);
            ivCommentItemAvatar = itemView.findViewById(R.id.ivCommentItemAvatar);

            itemView.setOnLongClickListener(v -> {
                onTouchViewItem.onTouch(getAdapterPosition(), itemView);
                return true;
            });
        }
    }
}
