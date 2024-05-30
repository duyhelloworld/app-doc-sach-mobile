package huce.edu.vn.appdocsach.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.adapters.BookAdapter;
import huce.edu.vn.appdocsach.adapters.HistoryAdapter;
import huce.edu.vn.appdocsach.apiservices.AuthService;
import huce.edu.vn.appdocsach.apiservices.BookService;
import huce.edu.vn.appdocsach.callbacks.OnApiResult;
import huce.edu.vn.appdocsach.callbacks.OnLoadMore;
import huce.edu.vn.appdocsach.configurations.SearchHistoryManager;
import huce.edu.vn.appdocsach.constants.IntentKey;
import huce.edu.vn.appdocsach.constants.UIConstants;
import huce.edu.vn.appdocsach.models.auth.AuthInfoModel;
import huce.edu.vn.appdocsach.models.book.FindBookModel;
import huce.edu.vn.appdocsach.models.book.SimpleBookModel;
import huce.edu.vn.appdocsach.models.paging.PagingResponse;
import huce.edu.vn.appdocsach.utils.AppLogger;
import huce.edu.vn.appdocsach.utils.DialogUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookSearchActivity extends AppCompatActivity implements OnLoadMore {

    SearchView svMainBookSearchBox;
    TextView tvEmptySearchHistory, tvBookSearchResultCount;
    RecyclerView rvBookSearchHistory;
    HistoryAdapter historyAdapter;
    Button btnClearHistory;
    BottomNavigationView bottom_navigation;
    AuthService authService = AuthService.authService;
    SearchHistoryManager historyManager = SearchHistoryManager.getInstance();
    BookService bookService = BookService.bookService;
    RecyclerView rvBookSearchList;
    BookAdapter bookAdapter;
    long totalPage = 0;
    AppLogger appLogger;
    FindBookModel findBookModel = new FindBookModel(UIConstants.NUMBER_BOOK_PER_REQUEST);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        svMainBookSearchBox = findViewById(R.id.svMainBookSearchBox);
        rvBookSearchHistory = findViewById(R.id.rvBookSearchHistory);
        tvEmptySearchHistory = findViewById(R.id.tvEmptySearchHistory);
        tvBookSearchResultCount = findViewById(R.id.tvBookSearchResultCount);
        btnClearHistory = findViewById(R.id.btnClearHistory);
        bottom_navigation = findViewById(R.id.bottom_navigation);
        rvBookSearchList = findViewById(R.id.rvBookSearchList);

        findBookModel.setKeyword("");
        tvEmptySearchHistory.setVisibility(View.GONE);
        svMainBookSearchBox.setOnClickListener(v -> {
            svMainBookSearchBox.setIconified(false);
        });

        showHistory();

        fetchBook(responseData -> {
            bookAdapter = new BookAdapter(responseData, rvBookSearchList, pos -> {
                Intent intent = new Intent(BookSearchActivity.this, BookDetailActivity.class);
                intent.putExtra(IntentKey.BOOK_ID, bookAdapter.getBookByPosition(pos).getId());
                startActivity(intent);
            }, BookSearchActivity.this);
            rvBookSearchList.setAdapter(bookAdapter);
        });

        svMainBookSearchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                findBookModel.setKeyword(query);
                bookService.getAllBook(findBookModel.getRetrofitQuery()).enqueue(new Callback<PagingResponse<SimpleBookModel>>() {
                    @Override
                    public void onResponse(@NonNull Call<PagingResponse<SimpleBookModel>> call, @NonNull Response<PagingResponse<SimpleBookModel>> response) {
                        PagingResponse<SimpleBookModel> data = response.body();
                        assert data != null;
                        totalPage = data.getTotalPage();
                        bookAdapter.setData(data.getValues());
                        historyManager.addSearchTerm(query);
                        showHistory();
                    }

                    @Override
                    public void onFailure(@NonNull Call<PagingResponse<SimpleBookModel>> call, @NonNull Throwable throwable) {
                        DialogUtils.errorUserSee(BookSearchActivity.this, R.string.error_load_book);
                        AppLogger.getInstance().error(throwable);
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        btnClearHistory.setOnClickListener(v -> {
            historyManager.clearHistory();
            if (historyAdapter != null) {
                historyAdapter.clear();
            }
            tvEmptySearchHistory.setVisibility(View.VISIBLE);
            Toast.makeText(this, R.string.clear_search_history, Toast.LENGTH_SHORT).show();
        });

        bottom_navigation.setSelectedItemId(R.id.navigation_search);
        bottom_navigation.setOnItemSelectedListener(menuItem -> {
            if(menuItem.getItemId() == R.id.navigation_home){
                Intent HomeIntent = new Intent(BookSearchActivity.this, MainActivity.class);
                startActivity(HomeIntent);
            }
            if(menuItem.getItemId() == R.id.navigation_categories){
                Intent CateIntent = new Intent(BookSearchActivity.this, CategoryTab.class);
                startActivity(CateIntent);
            }

            if(menuItem.getItemId() == R.id.navigation_user){
                authService.getInfo().enqueue(new Callback<AuthInfoModel>() {
                    @Override
                    public void onResponse(Call<AuthInfoModel> call, @NonNull Response<AuthInfoModel> response) {
                        AuthInfoModel model = response.body();
                        if(!response.isSuccessful()){
                            Intent loginIntent = new Intent(BookSearchActivity.this, LoginActivity.class);
                            startActivity(loginIntent);
                            return;
                        }
                        Intent intent = new Intent(BookSearchActivity.this, UserSettingActivity.class);
                        intent.putExtra(IntentKey.USER_AVATAR, model.getAvatar());
                        intent.putExtra(IntentKey.USER_FULLNAME, model.getFullname());
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<AuthInfoModel> call, Throwable throwable) {
                        DialogUtils.errorUserSee(BookSearchActivity.this, R.string.error_login);
                        appLogger.error(throwable);
                    }
                });
                return true;
            }
            return false;
        });

    }

    @Override
    public void loadMore() {
        if (findBookModel.getPageNumber() < totalPage) {
            new Handler().postDelayed(() -> {
                findBookModel.incrementPageNumber();
                fetchBook(successData -> {
                    bookAdapter.setData(successData);
                    bookAdapter.setLoaded();
                });
            }, 1500);
        }
    }

    private void showHistory() {
        List<String> histories = historyManager.getHistory();
        if (histories.isEmpty()) {
            tvEmptySearchHistory.setVisibility(View.VISIBLE);
            rvBookSearchHistory.setVisibility(View.GONE);
        } else {
            tvEmptySearchHistory.setVisibility(View.GONE);
            rvBookSearchHistory.setVisibility(View.VISIBLE);
            if (historyAdapter == null) {
                historyAdapter = new HistoryAdapter(histories, position -> {
                    svMainBookSearchBox.setQuery(historyAdapter.getHistory(position), false);
                    svMainBookSearchBox.requestFocus();
                    getSystemService(InputMethodManager.class).showSoftInput(svMainBookSearchBox, 0);
                });
                rvBookSearchHistory.setAdapter(historyAdapter);
            } else {
                historyAdapter.setData(histories);
            }
        }
    }

    private void fetchBook(OnApiResult<List<SimpleBookModel>> onSuccess) {
        BookService.bookService.getAllBook(findBookModel.getRetrofitQuery()).enqueue(new Callback<PagingResponse<SimpleBookModel>>() {
            @Override
            public void onResponse(@NonNull Call<PagingResponse<SimpleBookModel>> call, @NonNull Response<PagingResponse<SimpleBookModel>> response) {
                PagingResponse<SimpleBookModel> data = response.body();
                assert data != null;
                totalPage = data.getTotalPage();
                onSuccess.onSuccessCode(data.getValues());
            }

            @Override
            public void onFailure(@NonNull Call<PagingResponse<SimpleBookModel>> call, @NonNull Throwable throwable) {
                DialogUtils.errorUserSee(BookSearchActivity.this, R.string.error_load_book);
                AppLogger.getInstance().error(throwable);
            }
        });
    }
}
