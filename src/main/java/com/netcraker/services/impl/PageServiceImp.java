package com.netcraker.services.impl;

import com.netcraker.services.PageService;
import org.springframework.stereotype.Service;

@Service
public class PageServiceImp implements PageService {

    @Override
    public int getPagesCount(int total, int pageSize) {
        return Math.max(1, (int) Math.ceil((double) total / pageSize));
    }

    @Override
    public int getRestrictedPage(int page, int pagesCount) {
        return Math.min(pagesCount, Math.max(1, page));
    }
}
