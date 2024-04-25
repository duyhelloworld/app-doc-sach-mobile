package huce.edu.vn.appdocsach.configurations;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import huce.edu.vn.appdocsach.R;

public class ImageLoader {
    private final Picasso picasso;
    public ImageLoader(Context context) {
        this.picasso = new Picasso.Builder(context).build();
    }

    public void renderWithCache(String url, ImageView imageView) {
        picasso.load(url).into(imageView);
    }

    public void renderOnce(String url, ImageView imageView) {
        picasso.load(url)
                .networkPolicy(NetworkPolicy.NO_STORE)
                .memoryPolicy(MemoryPolicy.NO_STORE)
                .into(imageView);
    }

    public void renderWithoutCache(String url, ImageView imageView) {
        picasso.load(url)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(imageView);
    }

    public void renderChapter(String url, ImageView imageView) {
        picasso.load(url)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .resizeDimen(R.dimen.chapter_reader_width, R.dimen.chapter_reader_height)
                .into(imageView);
    }
}
