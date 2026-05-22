package com.ludoteca.tutorialauthor.author.model;

import com.ludoteca.tutorialauthor.common.pagination.PageableRequest;

public class AuthorSearchDto {

    private PageableRequest pageable;

    public PageableRequest getPageable() {
        return pageable;
    }

    public void setPageable(PageableRequest pageable) {
        this.pageable = pageable;
    }
}