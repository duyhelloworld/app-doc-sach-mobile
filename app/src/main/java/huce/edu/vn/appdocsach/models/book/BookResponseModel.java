package huce.edu.vn.appdocsach.models.book;

import com.google.gson.annotations.JsonAdapter;

import java.time.LocalDateTime;
import java.util.List;

import huce.edu.vn.appdocsach.models.category.SimpleCategoryModel;
import huce.edu.vn.appdocsach.utils.serializers.LocalDateTimeAdapter;

public class BookResponseModel {

    private Integer id;

    private String title;

    @JsonAdapter(value = LocalDateTimeAdapter.class)
    private LocalDateTime lastUpdatedAt;

    private String coverImage;

    private String author;

    private String description;

    private Double averageRate;

    private List<SimpleCategoryModel> categories;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAverageRate() {
        return averageRate;
    }

    public void setAverageRate(Double averageRate) {
        this.averageRate = averageRate;
    }

    public List<SimpleCategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(List<SimpleCategoryModel> categories) {
        this.categories = categories;
    }

    public BookResponseModel(Integer id, String title, LocalDateTime lastUpdatedAt, String coverImage, String author, String description, Double averageRate, List<SimpleCategoryModel> categories) {
        this.id = id;
        this.title = title;
        this.lastUpdatedAt = lastUpdatedAt;
        this.coverImage = coverImage;
        this.author = author;
        this.description = description;
        this.averageRate = averageRate;
        this.categories = categories;
    }
}
