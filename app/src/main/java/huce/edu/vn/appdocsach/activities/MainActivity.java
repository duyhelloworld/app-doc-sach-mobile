package huce.edu.vn.appdocsach.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.gson.Gson;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.adapters.BookAdapter;
import huce.edu.vn.appdocsach.apiservices.BookService;
import huce.edu.vn.appdocsach.models.book.BookResponseModel;
import huce.edu.vn.appdocsach.utils.DialogUtils;
import huce.edu.vn.appdocsach.utils.LoggerUtil;
import huce.edu.vn.appdocsach.utils.Pagination;
import huce.edu.vn.appdocsach.utils.serializers.GsonCustom;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvListBook;
    BookAdapter bookAdapter;
    BookService bookService = BookService.bookService;
    LoggerUtil log = LoggerUtil.getInstance();
    Gson gson = GsonCustom.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvListBook = findViewById(R.id.rvListBook);


        bookService.getAllBook().enqueue(new Callback<Pagination<BookResponseModel>>() {

            @Override
            public void onResponse(@NonNull Call<Pagination<BookResponseModel>> call, @NonNull Response<Pagination<BookResponseModel>> response) {
                DialogUtils.error(MainActivity.this, gson.toJson(response.toString()));
                if (!response.isSuccessful() || response.body() == null) {
                    return;
                }
                rvListBook.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                bookAdapter = new BookAdapter(response.body().getValues());
                rvListBook.setAdapter(bookAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<Pagination<BookResponseModel>> call, @NonNull Throwable throwable) {
                DialogUtils.error(MainActivity.this, throwable.getMessage());
                log.error((Exception) throwable);
            }
        });
    }
}