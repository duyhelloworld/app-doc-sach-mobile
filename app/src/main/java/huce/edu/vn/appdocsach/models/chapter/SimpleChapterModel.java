package huce.edu.vn.appdocsach.models.chapter;

import com.google.gson.annotations.JsonAdapter;

import java.io.Serializable;
import java.time.LocalDateTime;

import huce.edu.vn.appdocsach.models.BaseModel;
import huce.edu.vn.appdocsach.utils.serializers.LocalDateTimeAdapter;

public class SimpleChapterModel extends BaseModel implements Serializable {
    private String title;

    @JsonAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime lastUpdatedAt;

    public SimpleChapterModel(Integer id, String title, LocalDateTime lastUpdatedAt) {
        this.setId(id);
        this.title = title;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    @Override
    public boolean compareTo(Object o) {
        SimpleChapterModel s = (SimpleChapterModel) o;
        return getId() == s.getId() && title.equals(s.title) && lastUpdatedAt.equals(s.lastUpdatedAt);
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
