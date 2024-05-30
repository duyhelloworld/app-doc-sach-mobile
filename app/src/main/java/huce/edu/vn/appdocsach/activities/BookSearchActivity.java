package huce.edu.vn.appdocsach.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

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
import huce.edu.vn.appdocsach.apiservices.BookService;
import huce.edu.vn.appdocsach.callbacks.OnApiResult;
import huce.edu.vn.appdocsach.callbacks.OnLoadMore;
import huce.edu.vn.appdocsach.configurations.SearchHistoryManager;
import huce.edu.vn.appdocsach.constants.IntentKey;
import huce.edu.vn.appdocsach.constants.UIConstants;
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
    SearchHistoryManager historyManager = SearchHistoryManager.getInstance();
    RecyclerView rvBookSearchList;
    BookAdapter bookAdapter;
    List<SimpleBookModel> bookModels;
    long totalPage = 0;
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

        tvEmptySearchHistory.setVisibility(View.GONE);
        svMainBookSearchBox.setOnClickListener(v -> {
            svMainBookSearchBox.setIconified(false);
        });
        List<String> history = historyManager.getHistory();
        if (history.isEmpty()) {
            tvEmptySearchHistory.setVisibility(View.VISIBLE);
        } else {
            tvEmptySearchHistory.setVisibility(View.GONE);
            historyAdapter = new HistoryAdapter(historyManager.getHistory(), position -> {
                svMainBookSearchBox.setQuery(historyAdapter.getHistory(position), false);
                svMainBookSearchBox.clearFocus();
                svMainBookSearchBox.requestFocus();
                getSystemService(InputMethodManager.class).hideSoftInputFromWindow(svMainBookSearchBox.getWindowToken(), 0);
            });
            rvBookSearchHistory.setAdapter(historyAdapter);
        }

        svMainBookSearchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                historyManager.addSearchTerm(query);
                findBookModel.setKeyword(query);
                if (bookAdapter == null) {
                    fetchBook(responseData -> {
                        bookModels = responseData;
                        bookAdapter = new BookAdapter(bookModels, rvBookSearchList, pos -> {
                            Intent intent = new Intent(BookSearchActivity.this, BookDetailActivity.class);
                            intent.putExtra(IntentKey.BOOK_ID, bookAdapter.getBookByPosition(pos).getId());
                            startActivity(intent);
                        }, BookSearchActivity.this);
                        rvBookSearchList.setAdapter(bookAdapter);
                        findBookModel.setKeyword("");
                    });
                } else {
                    bookAdapter.setData(bookModels);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        btnClearHistory.setOnClickListener(v -> {
            historyManager.clearHistory();
            historyAdapter.clear();
            tvEmptySearchHistory.setVisibility(View.VISIBLE);
        });

        bottom_navigation.setSelectedItemId(R.id.navigation_search);
    }

    @Override
    public void loadMore() {
        if (findBookModel.getPageNumber() < totalPage) {
            new Handler().postDelayed(() -> {
                findBookModel.incrementPageNumber();
                fetchBook(responseData -> {
                    bookModels = responseData;
                    bookAdapter.setData(bookModels);
                    bookAdapter.setLoaded();
                });
            }, 1500);
        }
    }

    private void fetchBook(OnApiResult<List<SimpleBookModel>> onApiResult) {
        BookService.bookService.getAllBook(findBookModel.getRetrofitQuery()).enqueue(new Callback<PagingResponse<SimpleBookModel>>() {
            @Override
            public void onResponse(@NonNull Call<PagingResponse<SimpleBookModel>> call, @NonNull Response<PagingResponse<SimpleBookModel>> response) {
                PagingResponse<SimpleBookModel> data = response.body();
                assert data != null;
                totalPage = data.getTotalPage();
                onApiResult.onSuccessCode(data.getValues());
            }

            @Override
            public void onFailure(@NonNull Call<PagingResponse<SimpleBookModel>> call, @NonNull Throwable throwable) {
                DialogUtils.errorUserSee(BookSearchActivity.this, R.string.error_load_book);
                AppLogger.getInstance().error(throwable);
            }
        });
    }
}
