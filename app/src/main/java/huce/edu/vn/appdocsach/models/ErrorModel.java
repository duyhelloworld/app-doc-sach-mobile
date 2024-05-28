package huce.edu.vn.appdocsach.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.JsonAdapter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import huce.edu.vn.appdocsach.constants.DefaultError;
import huce.edu.vn.appdocsach.utils.serializers.LocalDateTimeAdapter;

public class ErrorModel {
    private Integer code = DefaultError.UNEXPECTED_ERROR.getCode();
    private List<String> messages = Collections.singletonList(DefaultError.UNEXPECTED_ERROR.getMessage());
    @JsonAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Override
    @NonNull
    public String toString() {
        return "ErrorModel{" +
                "messages=" + messages +
                '}';
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
