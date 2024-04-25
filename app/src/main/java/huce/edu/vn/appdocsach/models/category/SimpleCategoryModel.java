package huce.edu.vn.appdocsach.models.category;

import androidx.annotation.NonNull;

import huce.edu.vn.appdocsach.models.BaseModel;

public class SimpleCategoryModel extends BaseModel {
    private String name;

    public SimpleCategoryModel(Integer id, String name) {
        this.setId(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean compareTo(Object o) {
        SimpleCategoryModel s = (SimpleCategoryModel) o;
        return getId() == s.getId() && name.equals(s.name);
    }

    @NonNull
    @Override
    public String toString() {
        return "SimpleCategoryModel{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                '}';
    }
}
