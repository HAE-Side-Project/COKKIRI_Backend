package com.coggiri.main.mvc.repository;

import com.coggiri.main.mvc.domain.dto.GroupInfoDTO;
import com.coggiri.main.mvc.domain.dto.GroupRegisterDTO;
import com.coggiri.main.mvc.domain.entity.Group;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface GroupRepository {
    int createGroup(Group group);
    List<GroupInfoDTO> getGroupList(Map<String,Object> params);
}
