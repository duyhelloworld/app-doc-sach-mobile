package huce.edu.vn.appdocsach.apiservices;

import java.util.List;

import huce.edu.vn.appdocsach.configurations.RetrofitConfig;
import huce.edu.vn.appdocsach.models.category.CategoryResponse;
import huce.edu.vn.appdocsach.models.category.SimpleCategoryModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CategoryService {
    CategoryService categoryService = RetrofitConfig.getService(CategoryService.class, "api/category");

    @GET("all")
    Call<List<SimpleCategoryModel>> getAllCategories();

    @GET("find")
    Call<CategoryResponse> getCategoryInfo(@Query("id") int id);
}
