package huce.edu.vn.appdocsach.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.adapters.BookAdapter;
import huce.edu.vn.appdocsach.adapters.CategoryAdapter;
import huce.edu.vn.appdocsach.apiservices.AuthService;
import huce.edu.vn.appdocsach.apiservices.BookService;
import huce.edu.vn.appdocsach.apiservices.CategoryService;
import huce.edu.vn.appdocsach.callbacks.OnApiResult;
import huce.edu.vn.appdocsach.callbacks.OnLoadMore;
import huce.edu.vn.appdocsach.configurations.ImageLoader;
import huce.edu.vn.appdocsach.configurations.TokenStorageManager;
import huce.edu.vn.appdocsach.constants.IntentKey;
import huce.edu.vn.appdocsach.models.auth.AuthInfoModel;
import huce.edu.vn.appdocsach.models.book.SimpleBookModel;
import huce.edu.vn.appdocsach.models.category.SimpleCategoryModel;
import huce.edu.vn.appdocsach.models.paging.PagingResponse;
import huce.edu.vn.appdocsach.utils.AppLogger;
import huce.edu.vn.appdocsach.utils.DialogUtils;
import huce.edu.vn.appdocsach.models.book.FindBookModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnLoadMore {
    RecyclerView rvListBook, rvListCategory;

    BookAdapter bookAdapter;
    CategoryAdapter categoryAdapter;
    FindBookModel findBookModel = new FindBookModel(8);
    CategoryService categoryService = CategoryService.categoryService;
    BookService bookService = BookService.bookService;
    AuthService authService = AuthService.authService;
    ImageLoader imageLoader = ImageLoader.getInstance();
    NavigationBarView bottomNavigationView;
    TokenStorageManager tokenStorageManager = new TokenStorageManager();
    ProgressBar pbMain;
    SearchView svMainBookSearchBox;
    AppLogger appLogger = AppLogger.getInstance();
    int totalPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvListBook = findViewById(R.id.rvListBook);
        rvListCategory = findViewById(R.id.rvListCategory);
        pbMain = findViewById(R.id.pbMain);

        svMainBookSearchBox = findViewById(R.id.svMainBookSearchBox);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        svMainBookSearchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                findBookModel.setKeyword(query);
                loadBook(simpleBookModels -> {
                    if (simpleBookModels == null || simpleBookModels.size() == 0) {
                        DialogUtils.infoUserSee(MainActivity.this, R.string.not_found_book);
                        return;
                    }
                    bookAdapter.setData(simpleBookModels);
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                findBookModel.setKeyword(newText);
                loadBook(simpleBookModels -> {
                    if (simpleBookModels == null || simpleBookModels.size() == 0) {
                        DialogUtils.infoUserSee(MainActivity.this, R.string.not_found_book);
                        return;
                    }
                    bookAdapter.setData(simpleBookModels);
                });
                return true;
            }
        });



        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            if(menuItem.getItemId() == R.id.navigation_user){
                authService.getInfo().enqueue(new Callback<AuthInfoModel>() {
                    @Override
                    public void onResponse(Call<AuthInfoModel> call, @NonNull Response<AuthInfoModel> response) {
                        AuthInfoModel model = response.body();
                        if(!response.isSuccessful()){
                            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(loginIntent);
                            return;
                        }
                        Intent intent = new Intent(MainActivity.this, UserSettingActivity.class);
                        intent.putExtra(IntentKey.USER_AVATAR, model.getAvatar());
                        intent.putExtra(IntentKey.USER_FULLNAME, model.getFullname());
                        startActivity(intent);
                    }
                    @Override
                    public void onFailure(Call<AuthInfoModel> call, Throwable throwable) {
                        DialogUtils.errorUserSee(MainActivity.this, R.string.error_login);
                        appLogger.error(throwable);
                    }
                });
                return true;
            }
            return false;
        });


        new Handler().postDelayed(() -> {
            // load first page
            pbMain.setVisibility(View.VISIBLE);
            loadBook(simpleBookModels -> {
                bookAdapter = new BookAdapter(simpleBookModels, rvListBook,
                        position -> {
                            Intent intent = new Intent(MainActivity.this, BookDetailActivity.class);
                            intent.putExtra(IntentKey.BOOK_ID, bookAdapter.getBookByPosition(position).getId());
                            startActivity(intent);
                        }, this);
                rvListBook.setAdapter(bookAdapter);
            });

            categoryService.getAllCategories().enqueue(new Callback<List<SimpleCategoryModel>>() {
                @Override
                public void onResponse(@NonNull Call<List<SimpleCategoryModel>> call, @NonNull Response<List<SimpleCategoryModel>> response) {
                    categoryAdapter = new CategoryAdapter(response.body(), position -> {
                        findBookModel.setCategoryId(categoryAdapter.getData(position).getId());
                        loadBook(simpleBookModels -> bookAdapter.setData(simpleBookModels));
                    });
                    rvListCategory.setAdapter(categoryAdapter);
                }

                @Override
                public void onFailure(@NonNull Call<List<SimpleCategoryModel>> call, @NonNull Throwable throwable) {
                    DialogUtils.errorUserSee(MainActivity.this, R.string.error_load_category);
                    appLogger.error(throwable);
                }
            });
            pbMain.setVisibility(View.GONE);
        }, 500);
    }

    private void loadBook(OnApiResult<List<SimpleBookModel>> onApiResult) {
        bookService.getAllBook(findBookModel.getRetrofitQuery()).enqueue(new Callback<PagingResponse<SimpleBookModel>>() {
            @Override
            public void onResponse(@NonNull Call<PagingResponse<SimpleBookModel>> call, @NonNull Response<PagingResponse<SimpleBookModel>> response) {
                PagingResponse<SimpleBookModel> data = response.body();
                assert data != null;
                totalPage = data.getTotalPage();
                onApiResult.onSuccessCode(data.getValues());
            }

            @Override
            public void onFailure(@NonNull Call<PagingResponse<SimpleBookModel>> call, @NonNull Throwable throwable) {
                DialogUtils.errorUserSee(MainActivity.this, R.string.error_load_book);
                appLogger.error(throwable);
            }
        });
    }

    @Override
    public void loadMore() {
        if (findBookModel.getPageNumber() < totalPage) {
            pbMain.setVisibility(View.VISIBLE);
            new Handler().postDelayed(() -> {
                findBookModel.incrementPageNumber();
                loadBook(simpleBookModels -> bookAdapter.setData(simpleBookModels));
                bookAdapter.setLoaded();
                pbMain.setVisibility(View.GONE);
            }, 2000);
        } else {
            Toast.makeText(MainActivity.this, getString(R.string.loaded_final_book), Toast.LENGTH_SHORT).show();
        }
    }
}