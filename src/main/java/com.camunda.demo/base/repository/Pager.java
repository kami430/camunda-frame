package com.camunda.demo.base.repository;

import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Pager extends AbstractPageRequest {

    private Integer pageNumber;

    private Integer pageSize;

    private Sort sort;

    public static Pager of(int page, int size) {
        return of(page, size, Sort.unsorted());
    }

    public static Pager of(int page, int size, Sort sort) {
        return new Pager(page, size, sort);
    }

    public static Pager of(int page, int size, Sort.Direction direction, String... properties){
        return of(page,size,Sort.by(direction,properties));
    }

    protected Pager(int page, int size, Sort sort) {
        super(page, size);
        if (page < 1) throw new IllegalArgumentException("Page index must not be less than zero!");
        this.pageNumber = page;
        this.pageSize = size;
        this.sort = sort;
    }

    @Override
    public int getPageNumber() {
        return this.pageNumber;
    }

    @Override
    public int getPageSize() {
        return this.pageSize;
    }

    @Override
    public long getOffset() {
        return (long) (getPageNumber() - 1) * (long) (getPageSize());
    }

    @Override
    public Sort getSort() {
        return this.sort;
    }

    @Override
    public Pageable next() {
        return new Pager(getPageNumber() + 1, getPageSize(), getSort());
    }

    @Override
    public Pageable previous() {
        return getPageNumber() == 1 ? this : new Pager(getPageNumber() - 1, getPageSize(), getSort());
    }

    @Override
    public Pageable first() {
        return new Pager(1, getPageSize(), getSort());
    }

    @Override
    public boolean hasPrevious() {
        return getPageNumber() > 1;
    }
}
