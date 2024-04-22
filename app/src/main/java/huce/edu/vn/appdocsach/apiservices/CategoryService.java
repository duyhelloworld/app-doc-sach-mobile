package huce.edu.vn.appdocsach.apiservices;

import java.util.List;

import huce.edu.vn.appdocsach.configurations.RetrofitConfig;
import huce.edu.vn.appdocsach.models.category.CategoryResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryService {
    CategoryService categoryService = RetrofitConfig.getService(CategoryService.class, "api/category");

    @GET("all")
    Call<List<CategoryResponse>> getAllCategories();

    @GET("find")
    Call<CategoryResponse> getCategoryInfo(@Path("id") int id);
}
