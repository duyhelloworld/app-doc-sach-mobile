package huce.edu.vn.appdocsach.utils.serializers;

import com.google.gson.Gson;

import huce.edu.vn.appdocsach.models.ErrorModel;
import okhttp3.ResponseBody;

public class HttpErrorSerializer {
    private static final Gson gson = JsonSerializer.getInstance();

    public static ErrorModel extractError(ResponseBody responseBody) {
        if (responseBody == null) {
            return new ErrorModel();
        }
        return gson.fromJson(responseBody.charStream(), ErrorModel.class);
    }

    public static String extractErrorMessage(ResponseBody responseBody) {
        return String.join("\n", extractError(responseBody).getMessages());
    }
}
