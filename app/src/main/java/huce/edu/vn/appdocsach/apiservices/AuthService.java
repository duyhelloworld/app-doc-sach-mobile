package huce.edu.vn.appdocsach.apiservices;

import huce.edu.vn.appdocsach.configurations.RetrofitConfig;
import huce.edu.vn.appdocsach.models.auth.AuthInfoModel;
import huce.edu.vn.appdocsach.models.auth.AuthResponseModel;
import huce.edu.vn.appdocsach.models.auth.ChangePassRequestModel;
import huce.edu.vn.appdocsach.models.auth.SignInRequestModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface AuthService {
    AuthService authService = RetrofitConfig.getService(AuthService.class, "api/auth");

    @POST("signin")
    Call<AuthResponseModel> signIn(@Body SignInRequestModel signInRequestModel);

    @Multipart
    @POST("signup")
    Call<AuthResponseModel> signUpWithAvatar(@Part("jsonModel") RequestBody jsonModel, @Part MultipartBody.Part avatar);

    @GET("info")
    Call<AuthInfoModel> getInfo();

    @POST("signout")
    Call<Void> signOut();

    @PUT("change-pass")
    Call<Void> changePass(@Body ChangePassRequestModel changePassRequestModel);
    @Multipart
    @PUT("profile")
    Call<Void> changeProfile(@Part("jsonDto") AuthInfoModel jsonModel, @Part MultipartBody.Part avatar);

}
