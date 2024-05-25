package huce.edu.vn.appdocsach.apiservices;

import huce.edu.vn.appdocsach.configurations.RetrofitConfig;
import huce.edu.vn.appdocsach.models.auth.AuthInfoModel;
import huce.edu.vn.appdocsach.models.auth.AuthResponseModel;
import huce.edu.vn.appdocsach.models.auth.SignInRequestModel;
import huce.edu.vn.appdocsach.models.auth.SignUpRequestModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AuthService {
    AuthService authService = RetrofitConfig.getService(AuthService.class, "api/auth");

    @POST("signin")
    Call<AuthResponseModel> signIn(@Body SignInRequestModel signInRequestModel);

    @POST("signup/default")
    Call<AuthResponseModel> signUpWithoutAvatar(@Body SignUpRequestModel signUpRequestModel);

    @POST("signup")
    Call<AuthResponseModel> signUpWithAvatar(@Part String jsonModel, @Part Multipart avatar);

    @GET("info")
    Call<AuthInfoModel> getInfo();

    @POST("signout")
    Call<Void> signOut();
}
