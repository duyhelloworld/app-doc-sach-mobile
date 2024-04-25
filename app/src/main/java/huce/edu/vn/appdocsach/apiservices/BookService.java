package huce.edu.vn.appdocsach.apiservices;

import java.util.Map;

import huce.edu.vn.appdocsach.configurations.RetrofitConfig;
import huce.edu.vn.appdocsach.models.book.BookModel;
import huce.edu.vn.appdocsach.models.book.SimpleBookModel;
import huce.edu.vn.appdocsach.models.paging.PagingResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface BookService {
    BookService bookService = RetrofitConfig.getService(BookService.class, "api/book");

    @GET("all")
    Call<PagingResponse<SimpleBookModel>> getAllBook(@QueryMap Map<String, Object> findBookModel);

    @GET("find/{id}")
    Call<BookModel> getBookById(@Path("id") int id);
}
