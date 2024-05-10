package huce.edu.vn.appdocsach.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.adapters.CategoryAdapter;
import huce.edu.vn.appdocsach.adapters.ChapterAdapter;
import huce.edu.vn.appdocsach.apiservices.BookService;
import huce.edu.vn.appdocsach.configurations.ImageLoader;
import huce.edu.vn.appdocsach.constants.IntentKey;
import huce.edu.vn.appdocsach.models.book.BookModel;
import huce.edu.vn.appdocsach.models.chapter.OnlyNameChapterModel;
import huce.edu.vn.appdocsach.utils.DialogUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailActivity extends AppCompatActivity {

    TextView tvBookDetailTitle, tvBookDetailAuthorV,tvBookDetailDescriptionV;
    RecyclerView rvBookDetailChapterList, rvBookDetailCategories;
    RatingBar rbBookDetailRating;
    ImageView ivBookDetailCover;
    Button btnBookDetailReadFirst, btnBookDetailReadLast;
    ChapterAdapter chapterAdapter;
    CategoryAdapter categoryAdapter;
    BookService bookService = BookService.bookService;
    ImageLoader imageLoader;

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

        btnBookDetailReadFirst = findViewById(R.id.btnBookDetailReadFirst);
        btnBookDetailReadLast = findViewById(R.id.btnBookDetailReadLast);

        imageLoader = new ImageLoader(BookDetailActivity.this);

        Intent intent = getIntent();
        int bookId = intent.getIntExtra(IntentKey.BOOK_ID, 0);

        bookService.getBookById(bookId).enqueue(new Callback<BookModel>() {
            @Override
            public void onResponse(@NonNull Call<BookModel> call, @NonNull Response<BookModel> response) {
                BookModel model = response.body();
                if (response.isSuccessful() && model != null) {
                    imageLoader.renderWithCache(model.getCoverImage(), ivBookDetailCover);
                    tvBookDetailTitle.setText(model.getTitle());
                    tvBookDetailAuthorV.setText(model.getAuthor());
                    tvBookDetailDescriptionV.setText(model.getDescription());
                    rbBookDetailRating.setRating(model.getAverageRate());

                    categoryAdapter = new CategoryAdapter(model.getCategories(), position -> {
                        Intent intent = new Intent(BookDetailActivity.this, MainActivity.class);
                        intent.putExtra(IntentKey.CATEGORY_ID, categoryAdapter.getData(position).getId());
                        startActivity(intent);
                    });
                    rvBookDetailCategories.setAdapter(categoryAdapter);

                    chapterAdapter = new ChapterAdapter(model.getChapters(), position -> {
                        readChapter(position, chapterAdapter.getOnlyNamModel());
                    });
                    rvBookDetailChapterList.setAdapter(chapterAdapter);

                    btnBookDetailReadFirst.setOnClickListener(v -> {
                        readChapter(chapterAdapter.getFirstItemIndex(), chapterAdapter.getOnlyNamModel());
                    });

                    btnBookDetailReadLast.setOnClickListener(v -> {
                        readChapter(chapterAdapter.getLastItemIndex(), chapterAdapter.getOnlyNamModel());
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookModel> call, @NonNull Throwable throwable) {
                DialogUtils.developmentError(BookDetailActivity.this, throwable);
            }
        });
    }

    private void readChapter(int targetPos, List<OnlyNameChapterModel> chapterModels) {
        Intent intent = new Intent(BookDetailActivity.this, ChapterReaderActivity.class);
        intent.putExtra(IntentKey.CHAPTER_ID_POSITION, targetPos);
        intent.putParcelableArrayListExtra(IntentKey.OTHER_CHAPTER, new ArrayList<>(chapterModels));
        startActivity(intent);
    }
}