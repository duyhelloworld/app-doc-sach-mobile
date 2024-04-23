package huce.edu.vn.appdocsach.models.book;

import com.google.gson.annotations.JsonAdapter;

import java.time.LocalDateTime;
import java.util.List;

import huce.edu.vn.appdocsach.models.category.SimpleCategoryModel;
import huce.edu.vn.appdocsach.models.chapter.SimpleChapterResponseModel;
import huce.edu.vn.appdocsach.utils.serializers.LocalDateTimeAdapter;

public class BookResponseModel extends SimpleBookResponseModel {

    private String description;

    private Float averageRate;

    private List<SimpleChapterResponseModel> chapters;

    private List<SimpleCategoryModel> categories;

    public BookResponseModel(Integer id, String title, LocalDateTime lastUpdatedAt, String coverImage, String author, String description, Float averageRate, List<SimpleChapterResponseModel> chapters, List<SimpleCategoryModel> categories) {
        super(id, title, lastUpdatedAt, coverImage, author);
        this.description = description;
        this.averageRate = averageRate;
        this.chapters = chapters;
        this.categories = categories;
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

    public List<SimpleChapterResponseModel> getChapters() {
        return chapters;
    }

    public void setChapters(List<SimpleChapterResponseModel> chapters) {
        this.chapters = chapters;
    }


}
