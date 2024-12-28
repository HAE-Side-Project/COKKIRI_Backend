package com.coggiri.main.domain.group.model.dto.response;

import com.coggiri.main.domain.group.model.dto.request.GroupInfoDTO;

import java.util.List;

public record GroupListResponse(
        Long pageNum,
        Long totalNum,
        List<GroupInfoDTO> groupInfoDTOList
) {
    public static GroupListResponse of(Long pageNum,Long totalNum,List<GroupInfoDTO> groupInfoDTOList){
        return new GroupListResponse(
                pageNum,
                totalNum,
                groupInfoDTOList
        );
    }
}
