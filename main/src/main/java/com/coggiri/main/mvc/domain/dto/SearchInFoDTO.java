package com.coggiri.main.mvc.domain.dto;

import lombok.Getter;

@Getter
public class SearchInFoDTO {
    private String keyword;
    private int pageNum;

    public SearchInFoDTO(String keyword, int pageNum){
        this.keyword = keyword;
        this.pageNum = pageNum;
    }
}
