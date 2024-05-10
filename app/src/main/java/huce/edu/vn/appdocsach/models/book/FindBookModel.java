package huce.edu.vn.appdocsach.models.book;


import java.util.Map;

import huce.edu.vn.appdocsach.models.paging.RetrofitModel;
import huce.edu.vn.appdocsach.models.paging.RetrofitRequest;

public class FindBookModel extends RetrofitRequest implements RetrofitModel {
    private Integer categoryId = 0;

    private String keyword = "";

    public FindBookModel(Integer pageSize) {
        super(pageSize);
    }

    public Map<String, Object> getRetrofitQuery() {
        Map<String, Object> queryMap = super.getRetrofitQuery();
        queryMap.put("categoryId", categoryId);
        queryMap.put("keyword", keyword);
        return queryMap;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
