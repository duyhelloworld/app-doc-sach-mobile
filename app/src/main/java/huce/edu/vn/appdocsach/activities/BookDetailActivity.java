package huce.edu.vn.appdocsach.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.adapters.CategoryAdapter;
import huce.edu.vn.appdocsach.adapters.ChapterAdapter;
import huce.edu.vn.appdocsach.apiservices.BookService;
import huce.edu.vn.appdocsach.configurations.ImageLoader;
import huce.edu.vn.appdocsach.models.book.BookResponseModel;
import huce.edu.vn.appdocsach.utils.AlertType;
import huce.edu.vn.appdocsach.utils.DialogUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailActivity extends AppCompatActivity {

    TextView tvBookDetailTitle, tvBookDetailAuthorV,tvBookDetailDescriptionV;
    RecyclerView rvBookDetailChapterList, rvBookDetailCategories;
    RatingBar rbBookDetailRating;
    ImageView ivBookDetailCover;
    ChapterAdapter chapterAdapter;
    CategoryAdapter categoryAdapter;
    BookService bookService = BookService.bookService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        tvBookDetailAuthorV = findViewById(R.id.tvBookDetailAuthorV);
        tvBookDetailTitle = findViewById(R.id.tvBookDetailTitle);
        tvBookDetailDescriptionV = findViewById(R.id.tvBookDetailDescriptionV);

        rvBookDetailChapterList = findViewById(R.id.rvBookDetailChapterList);
        rvBookDetailCategories = findViewById(R.id.rvBookDetailCategories);

        rbBookDetailRating = findViewById(R.id.rbBookDetailRating);
        ivBookDetailCover = findViewById(R.id.ivBookDetailCover);

        Intent intent = getIntent();
        int bookId = intent.getIntExtra("id", 0);
        DialogUtils.show(AlertType.NOTIFICATION, BookDetailActivity.this, String.valueOf(bookId));

        bookService.getBookById(bookId).enqueue(new Callback<BookResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<BookResponseModel> call, @NonNull Response<BookResponseModel> response) {
                BookResponseModel model = response.body();
                if (response.isSuccessful() && model != null) {
                    ImageLoader.renderWithCache(model.getCoverImage(), ivBookDetailCover);
                    tvBookDetailTitle.setText(model.getTitle());
                    tvBookDetailAuthorV.setText(model.getAuthor());
                    tvBookDetailDescriptionV.setText(model.getDescription());
                    rbBookDetailRating.setRating(model.getAverageRate());

                    categoryAdapter = new CategoryAdapter(model.getCategories(), position -> {
                        Intent intent = new Intent(BookDetailActivity.this, MainActivity.class);
                        intent.putExtra("categoryId", categoryAdapter.getData(position).getId());
                        startActivity(intent);
                    });
                    rvBookDetailCategories.setAdapter(categoryAdapter);

                    chapterAdapter = new ChapterAdapter(model.getChapters(), position -> {
                        Intent intent = new Intent(BookDetailActivity.this, ChapterReaderActivity.class);
                        intent.putExtra("chapterId", chapterAdapter.getData(position).getId());
                        startActivity(intent);
                    });
                    rvBookDetailChapterList.setAdapter(chapterAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookResponseModel> call, @NonNull Throwable throwable) {
                DialogUtils.show(AlertType.ERROR, BookDetailActivity.this, throwable.getMessage());
            }
        });

    }
}