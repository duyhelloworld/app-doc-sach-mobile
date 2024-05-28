package huce.edu.vn.appdocsach.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.adapters.BookAdapter;
import huce.edu.vn.appdocsach.apiservices.AuthService;
import huce.edu.vn.appdocsach.apiservices.BookService;
import huce.edu.vn.appdocsach.callbacks.CallBack;
import huce.edu.vn.appdocsach.callbacks.OnApiResult;
import huce.edu.vn.appdocsach.callbacks.OnLoadMore;
import huce.edu.vn.appdocsach.configurations.ImageLoader;
import huce.edu.vn.appdocsach.configurations.TokenStorageManager;
import huce.edu.vn.appdocsach.constants.IntentKey;
import huce.edu.vn.appdocsach.constants.UIConstants;
import huce.edu.vn.appdocsach.models.auth.AuthInfoModel;
import huce.edu.vn.appdocsach.models.book.SimpleBookModel;
import huce.edu.vn.appdocsach.models.paging.PagingResponse;
import huce.edu.vn.appdocsach.utils.AppLogger;
import huce.edu.vn.appdocsach.utils.DialogUtils;
import huce.edu.vn.appdocsach.models.book.FindBookModel;
import huce.edu.vn.appdocsach.utils.StringUtils;
import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnLoadMore {
    RecyclerView rvListBook;
    ViewPager2 vp2ListHotBook;
    CircleIndicator3 ciListHotBook;
    ImageView ivMainAvatar;
    BookAdapter bookAdapter, hotBookAdapter;
    FindBookModel pagingRequest = new FindBookModel(UIConstants.NUMBER_BOOK_PER_REQUEST),
            findHotRequest = new FindBookModel(UIConstants.NUMBER_BOOK_INDICATOR, UIConstants.PROPERTY_TO_SORT_HOT_BOOK);
    BookService bookService = BookService.bookService;
    AuthService authService = AuthService.authService;
    ImageLoader imageLoader = ImageLoader.getInstance();
    TokenStorageManager tokenStorageManager = new TokenStorageManager();
    ProgressBar pbMain;
    SearchView svMainBookSearchBox;
    AppLogger appLogger = AppLogger.getInstance();
    List<SimpleBookModel> bookModels, hotBookModels;
    int totalPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvListBook = findViewById(R.id.rvListBook);
        pbMain = findViewById(R.id.pbMain);
        ivMainAvatar = findViewById(R.id.ivMainAvatar);
        svMainBookSearchBox = findViewById(R.id.svMainBookSearchBox);
        vp2ListHotBook = findViewById(R.id.vp2ListBook);
        ciListHotBook = findViewById(R.id.ciListHotBook);

        String token = tokenStorageManager.getAccessToken();
        if (StringUtils.hasText(token)) {
            fetchUserInfo(authInfoModel -> {
                if (tokenStorageManager.getIsFirstTime()) {
                    Toast.makeText(MainActivity.this, getString(R.string.welcome_login, authInfoModel.getFullname()),
                            Toast.LENGTH_SHORT).show();
                }
                imageLoader.show(authInfoModel.getAvatar(), ivMainAvatar);
                ivMainAvatar.setOnClickListener(l -> {
                    Intent intent = new Intent(MainActivity.this, UserSettingActivity.class);
                    intent.putExtra(IntentKey.USER_AVATAR, authInfoModel.getAvatar());
                    intent.putExtra(IntentKey.USER_FULLNAME, authInfoModel.getFullname());
                    startActivity(intent);
                });
            });
        } else {
            ivMainAvatar.setOnClickListener(l -> {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            });
        }

        new Handler().postDelayed(() -> {
            // load first open
            pbMain.setVisibility(View.VISIBLE);
            if (bookModels == null) {
                fetchBook(pagingRequest, () -> {
                    bookAdapter = new BookAdapter(bookModels, rvListBook,
                            this::openBookDetail, this);
                    rvListBook.setAdapter(bookAdapter);
                });
            } else {
                bookAdapter.setData(bookModels);
            }

            if (hotBookModels == null) {
                fetchBook(findHotRequest, () -> {
                    hotBookAdapter = new BookAdapter(bookModels, this::openBookDetail);
                    vp2ListHotBook.setAdapter(bookAdapter);
                });
            } else {
                hotBookAdapter.setData(hotBookModels);
            }

            vp2ListHotBook.setOffscreenPageLimit(3);
            vp2ListHotBook.setClipChildren(false);
            vp2ListHotBook.setClipToPadding(false);

            CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
            compositePageTransformer.addTransformer(new MarginPageTransformer(40));
            compositePageTransformer.addTransformer((page, position) -> {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            });
            vp2ListHotBook.setPageTransformer(compositePageTransformer);

            pbMain.setVisibility(View.GONE);
            ciListHotBook.setViewPager(vp2ListHotBook);
        }, 500);

        svMainBookSearchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void fetchBook(FindBookModel findBookModel, CallBack callBack) {
        bookService.getAllBook(findBookModel.getRetrofitQuery()).enqueue(new Callback<PagingResponse<SimpleBookModel>>() {
            @Override
            public void onResponse(@NonNull Call<PagingResponse<SimpleBookModel>> call, @NonNull Response<PagingResponse<SimpleBookModel>> response) {
                PagingResponse<SimpleBookModel> data = response.body();
                assert data != null;
                totalPage = data.getTotalPage();
                //                cache lại cho các lần sau
                bookModels = data.getValues();
                callBack.callBack();
            }

            @Override
            public void onFailure(@NonNull Call<PagingResponse<SimpleBookModel>> call, @NonNull Throwable throwable) {
                DialogUtils.errorUserSee(MainActivity.this, R.string.error_load_book);
                appLogger.error(throwable);
            }
        });
    }

    private void fetchUserInfo(OnApiResult<AuthInfoModel> onApiResult) {
        authService.getInfo().enqueue(new Callback<AuthInfoModel>() {
            @Override
            public void onResponse(@NonNull Call<AuthInfoModel> call, @NonNull Response<AuthInfoModel> response) {
                AuthInfoModel authModel = response.body();
                if (!response.isSuccessful() || authModel == null) {
                    //                    token hết hạn
                    ivMainAvatar.setOnClickListener(l -> {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    });
                    return;
                }
                onApiResult.onSuccessCode(authModel);
            }

            @Override
            public void onFailure(@NonNull Call<AuthInfoModel> call, @NonNull Throwable throwable) {
                DialogUtils.errorUserSee(MainActivity.this, R.string.error_load_user_info);
                appLogger.error(throwable);
            }
        });
    }

    @Override
    public void loadMore() {
        if (pagingRequest.getPageNumber() < totalPage) {
            pbMain.setVisibility(View.VISIBLE);
            new Handler().postDelayed(() -> {
                pagingRequest.incrementPageNumber();
                fetchBook(pagingRequest, () -> bookAdapter.setData(bookModels));
                bookAdapter.setLoaded();
                pbMain.setVisibility(View.GONE);
            }, 2000);
        }
    }

    private void search(String query) {
        if (StringUtils.isNullOrEmpty(query)) {
            if (bookModels == null || bookModels.size() == 0) {
                fetchBook(pagingRequest, () -> {
                    bookAdapter.setData(bookModels);
                });
            }
            return;
        }
        pagingRequest.setKeyword(query);
        fetchBook(pagingRequest, () -> {
            if (bookModels == null || bookModels.size() == 0) {
                DialogUtils.infoUserSee(MainActivity.this, R.string.not_found_book);
            }
            bookAdapter.setData(bookModels);
        });
    }

    private void openBookDetail(int position) {
        Intent intent = new Intent(MainActivity.this, BookDetailActivity.class);
        intent.putExtra(IntentKey.BOOK_ID, bookAdapter.getBookByPosition(position).getId());
        startActivity(intent);
    }
}