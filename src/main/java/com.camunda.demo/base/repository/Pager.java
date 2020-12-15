package com.camunda.demo.base.repository;

import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Pager<T> {
    private int page;
    private int size;
    private long total;
    private List<T> data;
    private long totalPage;
    private String sort;

    public static Pager of(int page, int size) {
        return of(page, size, null);
    }

    public static Pager of(int page, int size, String sort) {
        return new Pager(page, size, sort);
    }

    protected Pager(int page, int size, String sort) {
        if (page < 1) throw new IllegalArgumentException("Page index must not be less than zero!");
        this.page = page;
        this.size = size;
        this.sort = sort;
    }

    public Pager previous() {
        if (getPage() == 1) return this;
        this.page--;
        this.data = null;
        return this;
    }

    public Pager next() {
        this.page++;
        this.data = null;
        return this;
    }

    public long getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public Pager setTotal(long total) {
        this.total = total;
        this.setTotalPage(this.total % this.size > 0 ? this.total / this.size + 1 : this.total / this.size);
        return this;
    }

    public long getTotal() {
        return total;
    }

    private void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public Pager setData(List<T> data) {
        this.data = data;
        return this;
    }

    public List<T> getData() {
        return this.data;
    }

    public PageableImpl pageable() {
        if (null == this.sort || "".equals(this.sort.trim()))
            return PageableImpl.of(this.page, this.size);
        String[] sorts = this.sort.split(",");
        Sort sortable = Sort.by(Arrays.asList(sorts).stream().map(st -> {
            String[] starr = st.trim().split("\\s+");
            if (starr.length == 2) {
                if ("desc".equals(starr[1].trim().toLowerCase()))
                    return Sort.Order.desc(starr[0]);
                else return Sort.Order.asc(starr[0]);
            } else return Sort.Order.asc(st.trim());
        }).collect(Collectors.toList()));
        return PageableImpl.of(this.page, this.size, sortable);
    }

    public static class PageableImpl extends AbstractPageRequest {

        private Integer pageNumber;

        private Integer pageSize;

        private Sort sort;

        public static PageableImpl of(int page, int size) {
            return of(page, size, Sort.unsorted());
        }

        public static PageableImpl of(int page, int size, Sort sort) {
            return new PageableImpl(page, size, sort == null ? Sort.unsorted() : sort);
        }

        public static PageableImpl of(int page, int size, Sort.Direction direction, String... properties) {
            return of(page, size, Sort.by(direction, properties));
        }

        protected PageableImpl(int page, int size, Sort sort) {
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
        public PageableImpl next() {
            return new PageableImpl(getPageNumber() + 1, getPageSize(), getSort());
        }

        @Override
        public PageableImpl previous() {
            return getPageNumber() == 1 ? this : new PageableImpl(getPageNumber() - 1, getPageSize(), getSort());
        }

        @Override
        public PageableImpl first() {
            return new PageableImpl(1, getPageSize(), getSort());
        }

        @Override
        public boolean hasPrevious() {
            return getPageNumber() > 1;
        }
    }
}
