package huce.edu.vn.appdocsach.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.adapters.BookAdapter;
import huce.edu.vn.appdocsach.adapters.CategoryAdapter;
import huce.edu.vn.appdocsach.apiservices.BookService;
import huce.edu.vn.appdocsach.apiservices.CategoryService;
import huce.edu.vn.appdocsach.models.book.BookResponseModel;
import huce.edu.vn.appdocsach.models.category.CategoryResponse;
import huce.edu.vn.appdocsach.models.paging.PagingResponse;
import huce.edu.vn.appdocsach.utils.AppLogger;
import huce.edu.vn.appdocsach.utils.DialogUtils;
import huce.edu.vn.appdocsach.models.book.FindBookModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvListBook, rvListCategory;
    BookAdapter bookAdapter;
    CategoryAdapter categoryAdapter;
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

        categoryService.getAllCategories().enqueue(new Callback<List<CategoryResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<CategoryResponse>> call, @NonNull Response<List<CategoryResponse>> response) {
                categoryAdapter = new CategoryAdapter(response.body());
                rvListCategory.setAdapter(categoryAdapter);
                findBookModel = new FindBookModel(5, 1, "id", "Keyword");
            }

            @Override
            public void onFailure(@NonNull Call<List<CategoryResponse>> call, @NonNull Throwable throwable) {
                DialogUtils.error(MainActivity.this, throwable);
                log.error(throwable);
            }
        });


        bookService.getAllBook(findBookModel.getRequestForRetrofit()).enqueue(new Callback<PagingResponse<BookResponseModel>>() {
            @Override
            public void onResponse(@NonNull Call<PagingResponse<BookResponseModel>> call, @NonNull Response<PagingResponse<BookResponseModel>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    return;
                }
                bookAdapter = new BookAdapter(response.body().getValues());
                rvListBook.setAdapter(bookAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<PagingResponse<BookResponseModel>> call, @NonNull Throwable throwable) {
                DialogUtils.error(MainActivity.this, throwable);
                log.error(throwable);
            }
        });
    }
}