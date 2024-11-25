package com.coggiri.main.mvc.controller;

import com.coggiri.main.mvc.service.GroupService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "그룹 기능",description = "그룹 관련 API")
@RestController
@RequestMapping("/api/group")
public class GroupController {
    private GroupService groupService;

    @Autowired
    GroupController(GroupService groupService){
        this.groupService = groupService;
    }

    public ResponseEntity<Map<String,Object>> createGroup(){

    }
}
