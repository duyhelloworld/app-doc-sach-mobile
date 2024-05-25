package huce.edu.vn.appdocsach.callbacks;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import huce.edu.vn.appdocsach.utils.AppLogger;
import huce.edu.vn.appdocsach.utils.DialogUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitCallback<T> implements Callback<T> {
    private final OnRetrofitSuccess<T> onRetrofitSuccess;
    private final Context context;
    private final int errorMessageId;

    public RetrofitCallback(Context context, @StringRes int errorMessageId, OnRetrofitSuccess<T> onRetrofitSuccess) {
        this.context = context;
        this.onRetrofitSuccess = onRetrofitSuccess;
        this.errorMessageId = errorMessageId;
    }

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        onRetrofitSuccess.onSuccess(response);
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable throwable) {
        DialogUtils.errorUserSee(context, errorMessageId);
        AppLogger.getInstance().error(throwable);
    }
}
