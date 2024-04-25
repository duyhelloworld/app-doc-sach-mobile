package huce.edu.vn.appdocsach.models.book;

import com.google.gson.annotations.JsonAdapter;

import java.time.LocalDateTime;
import java.util.List;

import huce.edu.vn.appdocsach.models.BaseModel;
import huce.edu.vn.appdocsach.models.category.SimpleCategoryModel;
import huce.edu.vn.appdocsach.models.chapter.SimpleChapterModel;
import huce.edu.vn.appdocsach.utils.serializers.LocalDateTimeAdapter;

public class BookModel extends BaseModel {
    private String title;

    @JsonAdapter(value = LocalDateTimeAdapter.class)
    private LocalDateTime lastUpdatedAt;

    private String coverImage;

    private String author;

    private String description;

    private Float averageRate;

    private List<SimpleChapterModel> chapters;

    private List<SimpleCategoryModel> categories;

    @Override
    public boolean compareTo(Object o) {
        BookModel b = (BookModel) o;
        return b.getId() == getId() && b.title.equals(title) && b.author.equals(author) && b.averageRate.equals(averageRate);
    }

    public BookModel(Integer id, String title, LocalDateTime lastUpdatedAt, String coverImage, String author, String description, Float averageRate, List<SimpleChapterModel> chapters, List<SimpleCategoryModel> categories) {
        this.setId(id);
        this.title = title;
        this.lastUpdatedAt = lastUpdatedAt;
        this.coverImage = coverImage;
        this.author = author;
        this.description = description;
        this.averageRate = averageRate;
        this.chapters = chapters;
        this.categories = categories;
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

    public Float getAverageRate() {
        return averageRate;
    }

    public void setAverageRate(Float averageRate) {
        this.averageRate = averageRate;
    }

    public List<SimpleCategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(List<SimpleCategoryModel> categories) {
        this.categories = categories;
    }

    public List<SimpleChapterModel> getChapters() {
        return chapters;
    }

    public void setChapters(List<SimpleChapterModel> chapters) {
        this.chapters = chapters;
    }


}
