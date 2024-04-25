package huce.edu.vn.appdocsach.apiservices;

import java.util.List;

import huce.edu.vn.appdocsach.configurations.RetrofitConfig;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ChapterService {
    ChapterService chapterService = RetrofitConfig.getService(ChapterService.class, "api/chapter");

    @GET("find/{id}")
    Call<List<String>> read(@Path("id") int id);
}
