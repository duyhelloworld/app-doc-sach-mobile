package huce.edu.vn.appdocsach.apiservices;

import huce.edu.vn.appdocsach.configurations.RetrofitConfig;
import huce.edu.vn.appdocsach.models.book.BookResponseModel;
import huce.edu.vn.appdocsach.utils.Pagination;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BookService {
    BookService bookService = RetrofitConfig.getService(BookService.class, "api/book/");

    @GET("all")
    Call<Pagination<BookResponseModel>> getAllBook();

    @GET("find/{id}")
    Call<BookResponseModel> getBookById(@Path("id") int id);
}
