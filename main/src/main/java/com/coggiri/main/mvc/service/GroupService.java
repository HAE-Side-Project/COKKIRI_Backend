package com.coggiri.main.mvc.service;

import com.coggiri.main.mvc.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    @Autowired
    GroupService(GroupRepository groupRepository){
        this.groupRepository = groupRepository;
    }
}
