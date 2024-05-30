package huce.edu.vn.appdocsach.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.adapters.BookAdapter;
import huce.edu.vn.appdocsach.adapters.CategoryAdapter;
import huce.edu.vn.appdocsach.apiservices.AuthService;
import huce.edu.vn.appdocsach.apiservices.BookService;
import huce.edu.vn.appdocsach.apiservices.CategoryService;
import huce.edu.vn.appdocsach.callbacks.OnLoadMore;
import huce.edu.vn.appdocsach.constants.IntentKey;
import huce.edu.vn.appdocsach.constants.UIConstants;
import huce.edu.vn.appdocsach.models.auth.AuthInfoModel;
import huce.edu.vn.appdocsach.models.book.FindBookModel;
import huce.edu.vn.appdocsach.models.book.SimpleBookModel;
import huce.edu.vn.appdocsach.models.category.SimpleCategoryModel;
import huce.edu.vn.appdocsach.models.paging.PagingResponse;
import huce.edu.vn.appdocsach.utils.AppLogger;
import huce.edu.vn.appdocsach.utils.DialogUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryTab extends AppCompatActivity implements OnLoadMore {
    NavigationBarView bottomNavigationView;
    RecyclerView recyclerViewCategories, rcvBook;

    List<SimpleBookModel> bookModels;
    BookAdapter bookAdapter;
    FindBookModel findBookModel = new FindBookModel(UIConstants.NUMBER_BOOK_PER_REQUEST);
    BookService bookService = BookService.bookService;
    private CategoryAdapter categoryAdapter;
    AuthService authService = AuthService.authService;
    AppLogger appLogger = AppLogger.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_tab);
        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        bottomNavigationView =findViewById(R.id.bottom_navigation);
        rcvBook = findViewById(R.id.rcvBook);

        bottomNavigationView.setSelectedItemId(R.id.navigation_categories);

        bookService.getAllBook(findBookModel.getRetrofitQuery()).enqueue(new Callback<PagingResponse<SimpleBookModel>>() {
            @Override
            public void onResponse(Call<PagingResponse<SimpleBookModel>> call, Response<PagingResponse<SimpleBookModel>> response) {
                bookModels = response.body().getValues();
                bookAdapter = new BookAdapter(bookModels, rcvBook,
                        pos -> {
                            gotoBookDetail(bookAdapter.getBookByPosition(pos).getId());
                        }, () -> {});
                rcvBook.setAdapter(bookAdapter);
            }

            @Override
            public void onFailure(Call<PagingResponse<SimpleBookModel>> call, Throwable throwable) {

            }
        });

        categoryAdapter = new CategoryAdapter(new ArrayList<>(), position -> {
            findBookModel.setCategoryId(categoryAdapter.getData(position).getId());
            bookService.getAllBook(findBookModel.getRetrofitQuery()).enqueue(new Callback<PagingResponse<SimpleBookModel>>() {
                @Override
                public void onResponse(Call<PagingResponse<SimpleBookModel>> call, Response<PagingResponse<SimpleBookModel>> response) {
                    bookModels = response.body().getValues();
                    bookAdapter = new BookAdapter(bookModels, rcvBook,
                            pos -> {
                                gotoBookDetail(bookAdapter.getBookByPosition(pos).getId());
                            }, () -> {});
                    rcvBook.setAdapter(bookAdapter);
                }

                @Override
                public void onFailure(Call<PagingResponse<SimpleBookModel>> call, Throwable throwable) {

                }
            });
        });

        recyclerViewCategories.setAdapter(categoryAdapter);
        CategoryService.categoryService.getAllCategories().enqueue(new Callback<List<SimpleCategoryModel>>() {
            @Override
            public void onResponse(Call<List<SimpleCategoryModel>> call, Response<List<SimpleCategoryModel>> response) {
                if (response.isSuccessful()) {
                    List<SimpleCategoryModel> categories = response.body();
                    if (categories != null && !categories.isEmpty()) {
                        categoryAdapter.setData(categories);
                    } else {
                        Toast.makeText(CategoryTab.this, "Không có danh mục nào", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CategoryTab.this, "Lỗi khi lấy danh mục", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SimpleCategoryModel>> call, Throwable throwable) {
                Toast.makeText(CategoryTab.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            if(menuItem.getItemId() == R.id.navigation_home){
                Intent HomeIntent = new Intent(CategoryTab.this, MainActivity.class);
                startActivity(HomeIntent);
            }
            if(menuItem.getItemId() == R.id.navigation_categories){
                Intent CateIntent = new Intent(CategoryTab.this, CategoryTab.class);
                startActivity(CateIntent);
            }
            if(menuItem.getItemId() == R.id.navigation_search) {
                Intent HomeIntent = new Intent(CategoryTab.this, BookSearchActivity.class);
                startActivity(HomeIntent);
            }


            if(menuItem.getItemId() == R.id.navigation_user){
                authService.getInfo().enqueue(new Callback<AuthInfoModel>() {
                    @Override
                    public void onResponse(Call<AuthInfoModel> call, @NonNull Response<AuthInfoModel> response) {
                        AuthInfoModel model = response.body();
                        if(!response.isSuccessful()){
                            Intent loginIntent = new Intent(CategoryTab.this, LoginActivity.class);
                            startActivity(loginIntent);
                            return;
                        }
                        Intent intent = new Intent(CategoryTab.this, UserSettingActivity.class);
                        intent.putExtra(IntentKey.USER_AVATAR, model.getAvatar());
                        intent.putExtra(IntentKey.USER_FULLNAME, model.getFullname());
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<AuthInfoModel> call, Throwable throwable) {
                        DialogUtils.errorUserSee(CategoryTab.this, R.string.error_login);
                        appLogger.error(throwable);
                    }
                });
                return true;
            }
            return false;
        });



    }
    private void gotoBookDetail(int id) {
        Intent intent = new Intent(CategoryTab.this, BookDetailActivity.class);
        intent.putExtra(IntentKey.BOOK_ID, id);
        startActivity(intent);
    }

    @Override
    public void loadMore() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
