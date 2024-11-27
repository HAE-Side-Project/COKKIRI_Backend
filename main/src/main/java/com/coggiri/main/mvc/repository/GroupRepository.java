package com.coggiri.main.mvc.repository;

import com.coggiri.main.mvc.domain.dto.GroupRegisterDTO;
import com.coggiri.main.mvc.domain.entity.Group;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository {
    int createGroup(Group group);
}
