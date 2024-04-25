package huce.edu.vn.appdocsach.models.book;

import com.google.gson.annotations.JsonAdapter;

import java.time.LocalDateTime;

import huce.edu.vn.appdocsach.models.BaseModel;
import huce.edu.vn.appdocsach.utils.serializers.LocalDateTimeAdapter;

public class SimpleBookModel extends BaseModel {

    private String title;

    @JsonAdapter(value = LocalDateTimeAdapter.class)
    private LocalDateTime lastUpdatedAt;

    private String coverImage;

    private String author;

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

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public SimpleBookModel(Integer id, String title, LocalDateTime lastUpdatedAt, String coverImage, String author) {
        this.setId(id);
        this.title = title;
        this.lastUpdatedAt = lastUpdatedAt;
        this.coverImage = coverImage;
        this.author = author;
    }

    @Override
    public boolean compareTo(Object o) {
        SimpleBookModel s = (SimpleBookModel) o;
        return s.getId() == getId() && s.title.equals(title) && s.author.equals(author);
    }
}
