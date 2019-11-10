package com.netcraker.services;

public interface PageService {
    int getPagesCount(int total, int pageSize);
    int getRestrictedPage(int page, int pagesCount);
}
