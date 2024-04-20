package huce.edu.vn.appdocsach.models.paging;


import huce.edu.vn.appdocsach.constants.PaginationConstant;

public class PagingRequest {

    private Integer pageSize;

    private Integer pageNumber;

    private String sortBy;

    public PagingRequest() {
        this.pageSize = PaginationConstant.defaultPageSize;
        this.pageNumber = PaginationConstant.defaultPageNumber;
        this.sortBy = PaginationConstant.defaultSortBy;
    }

    public PagingRequest(Integer pageSize, Integer pageNumber, String sortBy) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.sortBy = sortBy;
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
