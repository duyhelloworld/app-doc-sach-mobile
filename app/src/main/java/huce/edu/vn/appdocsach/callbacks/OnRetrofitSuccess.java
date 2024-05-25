package huce.edu.vn.appdocsach.callbacks;

import retrofit2.Response;

public interface OnRetrofitSuccess<T> {
    void onSuccess(Response<T> response);
}
