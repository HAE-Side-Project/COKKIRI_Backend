package com.coggiri.main.mvc.controller;

import com.coggiri.main.mvc.domain.dto.GroupRegisterDTO;
import com.coggiri.main.mvc.service.GroupService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
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

    @PostMapping("/createGroup")
    public ResponseEntity<Map<String,Object>> createGroup( @RequestPart(value = "userId",required = true) String userId,
                                                           @RequestPart(value = "groupInfo",required = true) GroupRegisterDTO groupRegisterDTO,
                                                          @RequestPart(value = "image",required = false) MultipartFile image){
        Map<String,Object> response = new HashMap<>();

        System.out.println("userId:" + userId);

        if(groupService.createGroup(userId,groupRegisterDTO,image)){
            response.put("success",true);
            response.put("message","그룹 생성 완료");
        }else{
            response.put("success",false);
            response.put("message","그룹 생성 실패");
        }

        return ResponseEntity.ok(response);
    }
}
