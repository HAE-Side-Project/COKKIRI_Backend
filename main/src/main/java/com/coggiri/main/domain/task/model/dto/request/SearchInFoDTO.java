package com.coggiri.main.domain.task.model.dto.request;

import lombok.Getter;

@Getter
public class SearchInFoDTO {
    private String keyword;
    private Long pageNum;
    private Long offset = 100L;

    public SearchInFoDTO(String keyword, Long pageNum){
        this.keyword = keyword;
        this.pageNum = pageNum;
    }
}
