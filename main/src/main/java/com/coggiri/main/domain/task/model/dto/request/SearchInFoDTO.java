package com.coggiri.main.domain.task.model.dto.request;

import lombok.Getter;

@Getter
public class SearchInFoDTO {
    private String keyword;
    private int pageNum;
    private int offset;

    public SearchInFoDTO(String keyword, int pageNum,int offset){
        this.keyword = keyword;
        this.pageNum = pageNum;
        this.offset = offset;
    }
}
