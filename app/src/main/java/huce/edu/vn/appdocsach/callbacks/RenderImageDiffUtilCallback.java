package huce.edu.vn.appdocsach.callbacks;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class RenderImageDiffUtilCallback extends DiffUtil.Callback {
    private final List<String> oldUrls;
    private final List<String> newUrls;

    public RenderImageDiffUtilCallback(List<String> oldUrls, List<String> newUrls) {
        this.oldUrls = oldUrls;
        this.newUrls = newUrls;
    }

    @Override
    public int getOldListSize() {
        return oldUrls.size();
    }

    @Override
    public int getNewListSize() {
        return newUrls.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldUrls.get(oldItemPosition).substring(48)
                .equals(newUrls.get(newItemPosition).substring(48));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }
}
