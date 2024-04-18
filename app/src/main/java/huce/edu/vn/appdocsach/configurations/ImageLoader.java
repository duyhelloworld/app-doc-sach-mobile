package huce.edu.vn.appdocsach.configurations;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageLoader {
    private static final Picasso picasso = Picasso.get();

    public static void render(String url, ImageView imageView) {
        picasso.load(url).into(imageView);
    }
}
