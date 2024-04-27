package huce.edu.vn.appdocsach.models.book;


import java.util.HashMap;
import java.util.Map;

import huce.edu.vn.appdocsach.models.paging.PagingModel;
import huce.edu.vn.appdocsach.models.paging.PagingRequest;

public class FindBookModel extends PagingRequest implements PagingModel {
    private Integer categoryId;

    private String keyword;

    public FindBookModel(Integer categoryId, String keyword) {
        super();
        this.categoryId = categoryId;
        this.keyword = keyword;
    }

    public Map<String, Object> getRetrofitQuery() {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("categoryId", categoryId);
        queryMap.put("keyword", keyword);
        queryMap.put("pageSize", getPageSize());
        queryMap.put("pageNumber", getPageNumber());
        queryMap.put("sortBy", getSortBy());
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
