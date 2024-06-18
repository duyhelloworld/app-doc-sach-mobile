package huce.edu.vn.appdocsach.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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

public class CategoryFragment extends Fragment {
    NavigationBarView bottomNavigationView;
    RecyclerView recyclerViewCategories, rcvBook;

    List<SimpleBookModel> bookModels;
    BookAdapter bookAdapter;
    FindBookModel findBookModel = new FindBookModel(UIConstants.NUMBER_BOOK_PER_REQUEST);
    BookService bookService = BookService.bookService;
    private CategoryAdapter categoryAdapter;
    TextView tvCategory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_category_tab, container, false);
//        EdgeToEdge.enable(this);

        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        bottomNavigationView =view.findViewById(R.id.bottom_navigation);
        rcvBook = view.findViewById(R.id.rcvBook);
        tvCategory = view.findViewById(R.id.tvCategory);

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
            String selectedCategoryName = categoryAdapter.getData(position).getName();
            bookService.getAllBook(findBookModel.getRetrofitQuery()).enqueue(new Callback<PagingResponse<SimpleBookModel>>() {
                @Override
                public void onResponse(Call<PagingResponse<SimpleBookModel>> call, Response<PagingResponse<SimpleBookModel>> response) {
                    bookModels = response.body().getValues();
                    bookAdapter = new BookAdapter(bookModels, rcvBook,
                            pos -> {
                                gotoBookDetail(bookAdapter.getBookByPosition(pos).getId());
                            }, () -> {});
                    rcvBook.setAdapter(bookAdapter);
                    tvCategory.setText(selectedCategoryName);
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
                        Toast.makeText(getContext(), "Không có danh mục nào", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi khi lấy danh mục", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SimpleCategoryModel>> call, Throwable throwable) {
                Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void gotoBookDetail(int id) {
        Intent intent = new Intent(getContext(), BookDetailActivity.class);
        intent.putExtra(IntentKey.BOOK_ID, id);
        startActivity(intent);
    }
}
