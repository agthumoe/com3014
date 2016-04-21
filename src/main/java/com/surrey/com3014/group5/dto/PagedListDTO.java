package com.surrey.com3014.group5.dto;

import io.swagger.annotations.ApiModel;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Container object to hold paged list information.
 *
 * @author Aung Thu Moe
 */
@ApiModel
public class PagedListDTO<E> implements Serializable {
    private static final long serialVersionUID = 1174854900805976380L;

    /**
     * Default page size
     */
    private static final int DEFAULT_PAGE_SIZE = 10;
    /**
     * contents of this container. The list only contains data satisfing the pagedRequest.
     */
    private List<E> pagedList;
    /**
     * Total number of elements from the results.
     */
    private long numberOfElements;
    /**
     * Size of the page.
     */
    private int pageSize = DEFAULT_PAGE_SIZE;
    /**
     * Current page number.
     */
    private int pageNumber = 0;
    /**
     * Total number of available pages.
     */
    private long totalPages = 0;

    /**
     * Default constructor.
     */
    public PagedListDTO() {
        this.setPagedList(new ArrayList<>(0));
    }

    /**
     * Initialise new pageList container.
     *
     * @param pagedList contents of this container.
     */
    public PagedListDTO(List<E> pagedList) {
        this.setPagedList(pagedList);
    }

    /**
     * Get contents of this container.
     *
     * @return contents of this container.
     */
    public List<E> getPagedList() {
        return this.pagedList;
    }

    /**
     * Set contents of this container.
     *
     * @param pagedList contents to be set.
     */
    public void setPagedList(List<E> pagedList) {
        Assert.notNull(pagedList, "Page List must not be null");
        this.pagedList = pagedList;
    }

    /**
     * Get page size.
     *
     * @return page size.
     */
    public int getPageSize() {
        return this.pageSize;
    }

    /**
     * Set the size of a page.
     *
     * @param pageSize size of a page.
     */
    public void setPageSize(int pageSize) {
        Assert.state(pageSize >= 1, "pageSize must be >= 1");
        this.pageSize = pageSize;
    }

    /**
     * Get current page number.
     *
     * @return current page number.
     */
    public int getPageNumber() {
        return this.pageNumber;
    }

    /**
     * Set current page number
     *
     * @param pageNumber current page number.
     */
    public void setPageNumber(int pageNumber) {
        Assert.state(pageNumber >= 0, "pageNumber must be >= 0");
        this.pageNumber = pageNumber;
    }

    /**
     * Get total number of pages.
     *
     * @return total number of available pages.
     */
    public long getTotalPages() {
        return this.totalPages;
    }

    /**
     * Set total number of pages.
     *
     * @param totalPages total number of available pages.
     */
    public void setTotalPages(long totalPages) {
        Assert.state(totalPages >= 0, "totalPages must be >= 0");
        this.totalPages = totalPages;
    }

    /**
     * Check if the current page is first page.
     *
     * @return true if the current page is first page, false otherwise.
     */
    public boolean isFirstPage() {
        return getPageNumber() == 0;
    }

    /**
     * Check if the current page is last page.
     *
     * @return true if the current page is last page, false otherwise.
     */
    public boolean isLastPage() {
        return getPageNumber() == getTotalPages() - 1;
    }

    /**
     * Get total number of elements.
     *
     * @return total number of elements.
     */
    public long getNumberOfElements() {
        return this.numberOfElements;
    }

    /**
     * Set total number of elements.
     *
     * @param totalNumberOfElements total number of elements satisfing the query.
     */
    public void setNumberOfElements(long totalNumberOfElements) {
        Assert.state(totalNumberOfElements >= 0, "numberOfElements must be >= 0");
        this.numberOfElements = totalNumberOfElements;
    }
}
