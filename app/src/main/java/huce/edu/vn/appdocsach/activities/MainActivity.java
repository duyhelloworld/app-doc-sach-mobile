package huce.edu.vn.appdocsach.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.adapters.BookAdapter;
import huce.edu.vn.appdocsach.adapters.CategoryAdapter;
import huce.edu.vn.appdocsach.apiservices.BookService;
import huce.edu.vn.appdocsach.apiservices.CategoryService;
import huce.edu.vn.appdocsach.models.book.SimpleBookResponseModel;
import huce.edu.vn.appdocsach.models.category.SimpleCategoryModel;
import huce.edu.vn.appdocsach.models.paging.PagingResponse;
import huce.edu.vn.appdocsach.utils.AlertType;
import huce.edu.vn.appdocsach.utils.AppLogger;
import huce.edu.vn.appdocsach.utils.DialogUtils;
import huce.edu.vn.appdocsach.models.book.FindBookModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvListBook, rvListCategory;
    BookAdapter bookAdapter;
    CategoryAdapter<SimpleCategoryModel> categoryAdapter;
    FindBookModel findBookModel = new FindBookModel(0, "");
    CategoryService categoryService = CategoryService.categoryService;
    BookService bookService = BookService.bookService;
    AppLogger log = AppLogger.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvListBook = findViewById(R.id.rvListBook);
        rvListCategory = findViewById(R.id.rvListCategory);
        rvListCategory = findViewById(R.id.rvListCategory);

        categoryService.getAllCategories().enqueue(new Callback<List<SimpleCategoryModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<SimpleCategoryModel>> call, @NonNull Response<List<SimpleCategoryModel>> response) {
                categoryAdapter = new CategoryAdapter<>(response.body(), onTouchItem);
                rvListCategory.setAdapter(categoryAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<SimpleCategoryModel>> call, @NonNull Throwable throwable) {
                DialogUtils.show(AlertType.ERROR, MainActivity.this, throwable.getMessage());
                log.error(throwable);
            }
        });


        bookService.getAllBook(findBookModel.getRequestForRetrofit()).enqueue(new Callback<PagingResponse<SimpleBookResponseModel>>() {
            @Override
            public void onResponse(@NonNull Call<PagingResponse<SimpleBookResponseModel>> call, @NonNull Response<PagingResponse<SimpleBookResponseModel>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    return;
                }
                bookAdapter = new BookAdapter(response.body().getValues(),
                        position -> {
                            Intent intent = new Intent(MainActivity.this, BookDetailActivity.class);
                            intent.putExtra("id", bookAdapter.getBookByPosition(position).getId());
                            startActivity(intent);
                        });
                rvListBook.setAdapter(bookAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<PagingResponse<SimpleBookResponseModel>> call, @NonNull Throwable throwable) {
                DialogUtils.show(AlertType.ERROR, MainActivity.this, throwable.getMessage());
                log.error(throwable);
            }
        });
    }
}