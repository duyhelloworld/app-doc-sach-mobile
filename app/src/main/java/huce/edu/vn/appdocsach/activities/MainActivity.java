package huce.edu.vn.appdocsach.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
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

public class MainActivity extends AppCompatActivity {

    AuthService authService = AuthService.authService;
    NavigationBarView bottomNavigationView;
    FrameLayout flMainView;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        flMainView = findViewById(R.id.flMainView);

        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            int menuId = menuItem.getItemId();
            if(menuId == R.id.navigation_categories){
                fragment = new CategoryFragment();
            }
            if(menuId == R.id.navigation_user){
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
                        Bundle bundle = new Bundle();
                        bundle.putString(IntentKey.USER_AVATAR, model.getAvatar());
                        bundle.putString(IntentKey.USER_FULLNAME, model.getFullname());
                        fragment = new UserSettingFragment();
                        fragment.setArguments(bundle);
                    }

                    @Override
                    public void onFailure(@NonNull Call<AuthInfoModel> call, @NonNull Throwable throwable) {
                        DialogUtils.errorUserSee(MainActivity.this, R.string.error_login);
                    }
                });
                return true;
            }
            if (menuId == R.id.navigation_search) {
                fragment = new BookSearchFragment();
            }
            if (menuId == R.id.navigation_home) {
                fragment = new HomeFragment();
            }
            getSupportFragmentManager().beginTransaction().add(R.id.flMainView, fragment).commit();
            return false;
        });
    }
}