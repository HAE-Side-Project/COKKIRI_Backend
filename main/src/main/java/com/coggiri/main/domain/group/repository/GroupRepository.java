package com.coggiri.main.domain.group.repository;

import com.coggiri.main.mvc.domain.dto.GroupInfoDTO;
import com.coggiri.main.mvc.domain.entity.Group;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface GroupRepository {
    int createGroup(Group group);
    int deleteGroup(int groupId);
    int countGroupTotalNum();
    GroupInfoDTO getGroup(int groupId);
    List<GroupInfoDTO> getGroupList(Map<String,Object> params);
}
