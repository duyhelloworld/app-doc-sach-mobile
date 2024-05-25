package huce.edu.vn.appdocsach.apiservices;

import java.util.Map;

import huce.edu.vn.appdocsach.configurations.RetrofitConfig;
import huce.edu.vn.appdocsach.models.comment.SimpleCommentModel;
import huce.edu.vn.appdocsach.models.comment.WriteCommentModel;
import huce.edu.vn.appdocsach.models.paging.PagingResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface CommentService {
    CommentService commentService = RetrofitConfig.getService(CommentService.class, "api/comment");

    @GET("all")
    Call<PagingResponse<SimpleCommentModel>> getComments(@QueryMap Map<String, Object> findCommentModel);

    @POST("add")
    Call<SimpleCommentModel> writeComment(@Body WriteCommentModel writeCommentModel);
}
