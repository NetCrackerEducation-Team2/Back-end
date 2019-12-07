package com.netcraker.model;

public interface Pageable {
    static Pageable of(int page, int pageSize) {
        return new Pageable() {
            @Override
            public int getPage() {
                return page;
            }

            @Override
            public int getPageSize() {
                return pageSize;
            }
        };
    }

    int getPage();

    int getPageSize();
}