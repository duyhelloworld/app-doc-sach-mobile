package huce.edu.vn.appdocsach.models.category;

import androidx.annotation.NonNull;

import huce.edu.vn.appdocsach.models.BaseModel;

public class SimpleCategoryModel extends BaseModel {
    private String name;
    private boolean isSelected;

    public SimpleCategoryModel(Integer id, String name, boolean isSelected) {
        this.setId(id);
        this.name = name;
        this.isSelected = false;
    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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
