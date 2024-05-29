package huce.edu.vn.appdocsach.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.adapters.BookAdapter;
import huce.edu.vn.appdocsach.adapters.HotBookAdapter;
import huce.edu.vn.appdocsach.apiservices.AuthService;
import huce.edu.vn.appdocsach.apiservices.BookService;
import huce.edu.vn.appdocsach.callbacks.OnApiResult;
import huce.edu.vn.appdocsach.callbacks.OnLoadMore;
import huce.edu.vn.appdocsach.constants.IntentKey;
import huce.edu.vn.appdocsach.constants.UIConstants;
import huce.edu.vn.appdocsach.models.auth.AuthInfoModel;
import huce.edu.vn.appdocsach.models.book.FindBookModel;
import huce.edu.vn.appdocsach.models.book.SimpleBookModel;
import huce.edu.vn.appdocsach.models.paging.PagingResponse;
import huce.edu.vn.appdocsach.utils.AppLogger;
import huce.edu.vn.appdocsach.utils.DialogUtils;
import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnLoadMore {
    RecyclerView rvListBook;
    ViewPager2 vp2ListHotBook;
    CircleIndicator3 ciListHotBook;
    BookAdapter bookAdapter;
    HotBookAdapter hotBookAdapter;
    List<SimpleBookModel> bookModels, hotBookModels;
    FindBookModel findBookModel = new FindBookModel(UIConstants.NUMBER_BOOK_PER_REQUEST);
    BookService bookService = BookService.bookService;
    AuthService authService = AuthService.authService;
    NavigationBarView bottomNavigationView;
    ProgressBar pbMain;
    SearchView svMainBookSearchBox;
    AppLogger appLogger = AppLogger.getInstance();
    Handler changeHotBookHandler = new Handler(Looper.getMainLooper());
    Runnable changeBookRunnable = () -> {
        int cur = vp2ListHotBook.getCurrentItem();
        if (cur == hotBookModels.size() - 1) {
            vp2ListHotBook.setCurrentItem(0);
        } else {
            vp2ListHotBook.setCurrentItem(cur + 1);
        }
    };
    int totalPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvListBook = findViewById(R.id.rvListBook);
        vp2ListHotBook = findViewById(R.id.vp2ListHotBook);
        pbMain = findViewById(R.id.pbMain);
        ciListHotBook = findViewById(R.id.ciListHotBook);
        svMainBookSearchBox = findViewById(R.id.svMainBookSearchBox);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            if(menuItem.getItemId() == R.id.navigation_user){
                authService.getInfo().enqueue(new Callback<AuthInfoModel>() {
                    @Override
                    public void onResponse(@NonNull Call<AuthInfoModel> call, @NonNull Response<AuthInfoModel> response) {
                        AuthInfoModel model = response.body();
                        if (!response.isSuccessful()) {
                            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(loginIntent);
                            return;
                        }
                        assert model != null;
                        Intent intent = new Intent(MainActivity.this, UserSettingActivity.class);
                        intent.putExtra(IntentKey.USER_AVATAR, model.getAvatar());
                        intent.putExtra(IntentKey.USER_FULLNAME, model.getFullname());
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(@NonNull Call<AuthInfoModel> call, @NonNull Throwable throwable) {
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
            if (bookModels == null || bookAdapter == null) {
                fetchBook(responseData -> {
                    bookModels = responseData;
                    bookAdapter = new BookAdapter(bookModels, rvListBook,
                            pos -> gotoBookDetail(bookAdapter.getBookByPosition(pos).getId()), this);
                    rvListBook.setAdapter(bookAdapter);
                });
            } else {
                bookAdapter.setData(bookModels);
            }

            if (hotBookModels == null || hotBookAdapter == null) {
                findBookModel.setSort(UIConstants.PROPERTY_TO_SORT_HOT_BOOK);
                findBookModel.setPageNumber(1);
                findBookModel.setPageSize(UIConstants.NUMBER_BOOK_SHOW_SLIDE);
                fetchBook(responseData -> {
                    hotBookModels = responseData;
                    hotBookAdapter = new HotBookAdapter(hotBookModels, pos -> gotoBookDetail(hotBookAdapter.getBookByPosition(pos).getId()));
                    vp2ListHotBook.setAdapter(hotBookAdapter);
                    vp2ListHotBook.setOffscreenPageLimit(3);
                    CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                    compositePageTransformer.addTransformer(new MarginPageTransformer(40));
                    compositePageTransformer.addTransformer((page, position) -> page.setScaleY(0.85f + 0.15f * (1 - Math.abs(position))));
                    vp2ListHotBook.setPageTransformer(compositePageTransformer);
                    vp2ListHotBook.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                        @Override
                        public void onPageSelected(int position) {
                            super.onPageSelected(position);
                            changeHotBookHandler.removeCallbacks(changeBookRunnable);
                            changeHotBookHandler.postDelayed(changeBookRunnable, 2000);
                        }
                    });
                    ciListHotBook.setViewPager(vp2ListHotBook);
                });
            } else {
                hotBookAdapter.setData(hotBookModels);
            }
            pbMain.setVisibility(View.GONE);
        }, 500);

        svMainBookSearchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                findBookModel.setKeyword(query);
                fetchBook(responseData -> {
                    if (responseData == null || responseData.size() == 0) {
                        DialogUtils.infoUserSee(MainActivity.this, R.string.not_found_book);
                        return;
                    }
                    bookModels = responseData;
                    bookAdapter.setData(bookModels);
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeHotBookHandler.postDelayed(changeBookRunnable, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        changeHotBookHandler.removeCallbacks(changeBookRunnable);
    }

    private void fetchBook(OnApiResult<List<SimpleBookModel>> onApiResult) {
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
                //                DialogUtils.errorUserSee(MainActivity.this, R.string.error_load_book);
                DialogUtils.errorDevSee(MainActivity.this, "bug", throwable);
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
                fetchBook(responseData -> {
                    bookModels = responseData;
                    bookAdapter.setData(bookModels);
                    bookAdapter.setLoaded();
                    pbMain.setVisibility(View.GONE);
                });
            }, 2000);
        }
    }

    private void gotoBookDetail(int id) {
        Intent intent = new Intent(MainActivity.this, BookDetailActivity.class);
        intent.putExtra(IntentKey.BOOK_ID, id);
        startActivity(intent);
    }
}