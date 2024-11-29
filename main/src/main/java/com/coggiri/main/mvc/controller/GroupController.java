package com.coggiri.main.mvc.controller;

import com.coggiri.main.mvc.domain.dto.GroupInfoDTO;
import com.coggiri.main.mvc.domain.dto.SearchInFoDTO;
import com.coggiri.main.mvc.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Tag(name = "그룹 기능",description = "그룹 관련 Get Method")
//@Controller
//@RequestMapping("/group")
//public class GroupController {
//
//    @Autowired
//    private GroupService groupService;
//
//}
