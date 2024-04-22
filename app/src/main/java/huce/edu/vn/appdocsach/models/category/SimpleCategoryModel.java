package huce.edu.vn.appdocsach.models.category;

import androidx.annotation.NonNull;

public class SimpleCategoryModel {
    private Integer id;

    private String name;

    public SimpleCategoryModel(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return "SimpleCategoryModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
