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
import huce.edu.vn.appdocsach.constants.IntentKey;
import huce.edu.vn.appdocsach.models.book.SimpleBookModel;
import huce.edu.vn.appdocsach.models.category.SimpleCategoryModel;
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
    List<SimpleCategoryModel> categoryModels;
    FindBookModel findBookModel = new FindBookModel(10);
    CategoryService categoryService = CategoryService.categoryService;
    BookService bookService = BookService.bookService;

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
                categoryModels = response.body();
                categoryAdapter = new CategoryAdapter(categoryModels, position -> {
                    int categoryId = categoryAdapter.getData(position).getId();
                    loadByCategoryId(categoryId);
                });
                rvListCategory.setAdapter(categoryAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<SimpleCategoryModel>> call, @NonNull Throwable throwable) {
                DialogUtils.developmentError(MainActivity.this, throwable);
            }
        });


        bookService.getAllBook(findBookModel.getRetrofitQuery()).enqueue(new Callback<PagingResponse<SimpleBookModel>>() {
            @Override
            public void onResponse(@NonNull Call<PagingResponse<SimpleBookModel>> call, @NonNull Response<PagingResponse<SimpleBookModel>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    return;
                }
                bookAdapter = new BookAdapter(MainActivity.this, response.body().getValues(),
                        position -> {
                            Intent intent = new Intent(MainActivity.this, BookDetailActivity.class);
                            intent.putExtra(IntentKey.BOOK_ID, bookAdapter.getBookByPosition(position).getId());
                            startActivity(intent);
                        });
                rvListBook.setAdapter(bookAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<PagingResponse<SimpleBookModel>> call, @NonNull Throwable throwable) {
                DialogUtils.developmentError(MainActivity.this, throwable);
            }
        });
    }

    private void loadByCategoryId(int categoryId) {
        findBookModel.setCategoryId(categoryId);
        bookService.getAllBook(findBookModel.getRetrofitQuery()).enqueue(new Callback<PagingResponse<SimpleBookModel>>() {
            @Override
            public void onResponse(@NonNull Call<PagingResponse<SimpleBookModel>> call, @NonNull Response<PagingResponse<SimpleBookModel>> response) {
                PagingResponse<SimpleBookModel> data = response.body();
                if (bookAdapter != null && data != null) {
                    bookAdapter.setData(data.getValues());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PagingResponse<SimpleBookModel>> call, @NonNull Throwable throwable) {
                DialogUtils.developmentError(MainActivity.this, throwable);
            }
        });
    }
}