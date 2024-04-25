package huce.edu.vn.appdocsach.models.category;

import androidx.annotation.NonNull;

import huce.edu.vn.appdocsach.models.BaseModel;

public class CategoryModel extends BaseModel {
    private String name;

    private String description;

    public CategoryModel(Integer id, String name, String description) {
        this.setId(id);
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean compareTo(Object o) {
        CategoryModel c = (CategoryModel) o;
        return getId() == c.getId() && name.equals(c.name);
    }

    @NonNull
    @Override
    public String toString() {
        return "CategoryModel{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
