package huce.edu.vn.appdocsach.adapters;

import android.content.Context;
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
import huce.edu.vn.appdocsach.models.comment.SimpleCommentModel;
import huce.edu.vn.appdocsach.utils.DatetimeUtils;
import huce.edu.vn.appdocsach.utils.DialogUtils;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private final OnTouchItem onTouchItem;
    private final List<SimpleCommentModel> commentModels;
    private final ImageLoader imageLoader;
    public CommentAdapter(Context context, List<SimpleCommentModel> commentModels, OnTouchItem onTouchItem) {
        this.onTouchItem = onTouchItem;
        this.commentModels = commentModels;
        this.imageLoader = new ImageLoader(context);
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_comment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        SimpleCommentModel comment = commentModels.get(position);
        holder.tvCommentItemFullname.setText(comment.getUsername());
        holder.tvCommentItemLastUpdatedAtV.setText(DatetimeUtils.countTimeCostedUpToNow(comment.getCommentAt()));

        if (!comment.getEdited()) {
            holder.tvCommentItemIsEdited.setText("");
        } else {
            holder.tvCommentItemIsEdited.setText(R.string.is_edited);
        }

        String fullText = comment.getContent();
        String shortText = fullText.substring(0, holder.tvCommentItemContent.getMaxLines() - 1) + R.string.comment_content_see_more;
        holder.tvCommentItemContent.setText(shortText);
        imageLoader.renderWithCache(comment.getUserAvatar(), holder.ivCommentItemAvatar);
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
            tvCommentItemFullname.setOnClickListener(v -> onTouchItem.onClick(getAdapterPosition()));
        }
    }
}
