package huce.edu.vn.appdocsach.utils;

import huce.edu.vn.appdocsach.models.category.CategoryResponse;
import huce.edu.vn.appdocsach.models.category.SimpleCategoryModel;

public class ModelConverter {

    public static CategoryResponse convert(SimpleCategoryModel simpleCategoryModel) {
        return new CategoryResponse(simpleCategoryModel.getId(), simpleCategoryModel.getName(), "");
    }

    public static SimpleCategoryModel convert(CategoryResponse categoryResponse) {
        return new SimpleCategoryModel(categoryResponse.getId(), categoryResponse.getName());
    }
}
