package com.surrey.com3014.group5.dto;

import io.swagger.annotations.ApiModel;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aung Thu Moe
 */
@ApiModel
public class PagedListDTO<E> implements Serializable {
    private static final long serialVersionUID = 1174854900805976380L;
    public static final int DEFAULT_PAGE_SIZE = 10;

    private List<E> pagedList;
    private long numberOfElements;
    private int pageSize = DEFAULT_PAGE_SIZE;
    private int pageNumber = 0;
    private long totalPages = 0;

    public PagedListDTO() {
        this.setPagedList(new ArrayList<>(0));
    }

    public PagedListDTO(List<E> pagedList) {
        this.setPagedList(pagedList);
    }

    public void setPagedList(List<E> pagedList) {
        Assert.notNull(pagedList, "Page List must not be null");
        this.pagedList = pagedList;
    }

    public List<E> getPagedList() {
        return this.pagedList;
    }

    public void setPageSize(int pageSize) {
        Assert.state(pageSize >= 1, "pageSize must be >= 1");
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageNumber(int pageNumber) {
        Assert.state(pageNumber >= 0, "pageNumber must be >= 0");
        this.pageNumber = pageNumber;
    }

    public void setTotalPages(long totalPages) {
        Assert.state(totalPages >= 0, "totalPages must be >= 0");
        this.totalPages = totalPages;
    }

    public int getPageNumber() {
        return this.pageNumber;
    }

    public long getTotalPages() {
        return this.totalPages;
    }

    public boolean isFirstPage() {
        return getPageNumber() == 0;
    }

    public boolean isLastPage() {
        return getPageNumber() == getTotalPages() - 1;
    }

    public void setNumberOfElements(long totalNumberOfElements) {
        Assert.state(totalNumberOfElements >= 0, "numberOfElements must be >= 0");
        this.numberOfElements = totalNumberOfElements;
    }

    public long getNumberOfElements() {
        return this.numberOfElements;
    }
}
