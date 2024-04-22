package huce.edu.vn.appdocsach.configurations;

import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ImageLoader {
    private static final Picasso picasso = Picasso.get();

    public static void renderWithCache(String url, ImageView imageView) {
        picasso.load(url).into(imageView);
    }

    public static void renderOnce(String url, ImageView imageView) {
        picasso.load(url)
                .networkPolicy(NetworkPolicy.NO_STORE)
                .memoryPolicy(MemoryPolicy.NO_STORE)
                .into(imageView);
    }

    public static void renderWithoutCache(String url, ImageView imageView) {
        picasso.load(url)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(imageView);
    }
}
