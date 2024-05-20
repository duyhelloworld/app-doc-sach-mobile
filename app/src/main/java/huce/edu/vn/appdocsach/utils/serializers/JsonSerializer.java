package huce.edu.vn.appdocsach.utils.serializers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

import huce.edu.vn.appdocsach.models.ErrorModel;
import okhttp3.ResponseBody;

public class JsonSerializer {
    private static Gson gson;

    public static Gson getInstance() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .setPrettyPrinting()
                    .setLenient()
                    .disableHtmlEscaping()
                    .setVersion(1)
                    .create();
        }
        return gson;
    }
}
