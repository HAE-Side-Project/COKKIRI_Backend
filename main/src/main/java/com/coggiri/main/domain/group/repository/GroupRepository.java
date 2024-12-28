package com.coggiri.main.domain.group.repository;

import com.coggiri.main.domain.group.model.dto.request.GroupInfoDTO;
import com.coggiri.main.domain.group.model.entity.Group;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface GroupRepository {
    int createGroup(Group group);
    int deleteGroup(Long groupId);
    Long countGroupTotalNum();
    GroupInfoDTO getGroup(Long groupId);
    List<GroupInfoDTO> getGroupList(@Param("keyword") String keyword,@Param("limit") Long limit,@Param("offset") Long offset);
}
