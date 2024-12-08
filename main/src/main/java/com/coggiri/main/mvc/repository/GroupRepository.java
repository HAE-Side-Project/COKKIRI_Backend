package com.coggiri.main.mvc.repository;

import com.coggiri.main.mvc.domain.dto.GroupInfoDTO;
import com.coggiri.main.mvc.domain.entity.Group;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

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
