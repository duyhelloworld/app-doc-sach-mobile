package huce.edu.vn.appdocsach.configurations;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import huce.edu.vn.appdocsach.AppContext;

public class ImageLoader {
    private static ImageLoader instance;
    private final Picasso picasso = new Picasso.Builder(AppContext.getContext()).build();

    public static ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
        }
        return instance;
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
                .fit()
                .into(imageView);
    }
}
