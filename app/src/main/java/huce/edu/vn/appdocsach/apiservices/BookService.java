package huce.edu.vn.appdocsach.apiservices;

import java.util.Map;

import huce.edu.vn.appdocsach.configurations.RetrofitConfig;
import huce.edu.vn.appdocsach.models.book.BookResponseModel;
import huce.edu.vn.appdocsach.models.book.SimpleBookResponseModel;
import huce.edu.vn.appdocsach.models.paging.PagingResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface BookService {
    BookService bookService = RetrofitConfig.getService(BookService.class, "api/book");

    @GET("all")
    Call<PagingResponse<SimpleBookResponseModel>> getAllBook(@QueryMap Map<String, Object> findBookModel);

    @GET("find")
    Call<BookResponseModel> getBookById(@Query("id") int id);
}
