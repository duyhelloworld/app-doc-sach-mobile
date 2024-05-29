package huce.edu.vn.appdocsach.configurations;

import android.widget.ImageView;

import androidx.annotation.DrawableRes;

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

    public void showWithoutCache(String url, ImageView imageView) {
        picasso.load(url)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(imageView);
    }

    public void show(String url, ImageView imageView) {
        picasso.load(url).fit().into(imageView);
    }

    public void show(String url, ImageView imageView, @DrawableRes int placeHolderId, @DrawableRes int errorViewId) {
        picasso.load(url).fit().error(errorViewId).placeholder(placeHolderId).into(imageView);
    }

    public void showNoCacheNoStore(String url, ImageView imageView, @DrawableRes int placeholderId) {
        picasso.load(url)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_STORE)
                .placeholder(placeholderId)
                .into(imageView);
    }

    public void cancelRequest(ImageView imageView) {
        picasso.cancelRequest(imageView);
    }
}
