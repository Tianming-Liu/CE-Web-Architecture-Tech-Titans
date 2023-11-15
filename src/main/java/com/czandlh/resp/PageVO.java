package com.czandlh.resp;

public class PageVO {

    private Integer currentPage = 0;
    private Integer pageSize = 10;
    private Integer total = 0;

    public PageVO(Integer total, Integer pageSize) {
        this.pageSize = pageSize;
        this.total = total;
    }

    public PageVO(Integer total, Integer pageSize, Integer currentPage) {
        this.pageSize = pageSize;
        this.total = total;
        this.currentPage = currentPage;
    }

    public PageVO() {
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
