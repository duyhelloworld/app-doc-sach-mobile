package huce.edu.vn.appdocsach.models.paging;


import java.util.HashMap;
import java.util.Map;

import huce.edu.vn.appdocsach.constants.PaginationConstant;

public abstract class RetrofitRequest implements RetrofitModel {

    private Integer pageSize;

    private Integer pageNumber = PaginationConstant.defaultPageNumber;

    private String sortBy = PaginationConstant.defaultSortBy;

    public RetrofitRequest(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public Map<String, Object> getRetrofitQuery() {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("pageSize", getPageSize());
        queryMap.put("pageNumber", getPageNumber());
        queryMap.put("sortBy", getSortBy());
        return queryMap;
    }

    public void incrementPageNumber() {
        this.pageNumber++;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
