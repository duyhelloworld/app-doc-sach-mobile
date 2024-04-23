package huce.edu.vn.appdocsach.models.chapter;

import com.google.gson.annotations.JsonAdapter;

import java.time.LocalDateTime;

import huce.edu.vn.appdocsach.utils.serializers.LocalDateTimeAdapter;

public class SimpleChapterResponseModel {
    private Integer id;

    private String title;

    @JsonAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime lastUpdatedAt;

    public SimpleChapterResponseModel(Integer id, String title, LocalDateTime lastUpdatedAt) {
        this.id = id;
        this.title = title;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }
}
