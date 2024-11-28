package com.coggiri.main.mvc.controller;

import com.coggiri.main.mvc.domain.dto.GroupInfoDTO;
import com.coggiri.main.mvc.domain.dto.SearchInFoDTO;
import com.coggiri.main.mvc.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

@Controller
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Operation(summary = "그룹 리스트 정보 리스트", description = "그룹 정보 리스트 반환 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "그룹 리스트 반환 API",
                            content = @Content(
                                    schemaProperties = {
                                            @SchemaProperty(name = "success",schema = @Schema(type = "boolean",description = "성공 여부")),
                                            @SchemaProperty(name = "message",schema = @Schema(type = "string",description = "성공 및 오류메세지 반환")),
                                            @SchemaProperty(name = "groupList",schema = @Schema(implementation = GroupInfoDTO.class,description = "그룹 정보 리스트"))
                                    }
                            )
                    )
            }
    )
    @ResponseBody
    @GetMapping("/getGroupList")
    public ResponseEntity<Map<String,Object>> getGroupList(@RequestParam(name = "keyword", required = false) String keyword, @RequestParam(name = "pageNum", defaultValue = "1")  int pageNum){
        Map<String,Object> response = new HashMap<>();

        SearchInFoDTO searchInFoDTO = new SearchInFoDTO(keyword,pageNum);

        try {
            List<GroupInfoDTO> groupInfoList = groupService.getGroupList(searchInFoDTO);
            response.put("success",true);
            response.put("message","그룹 리스트 반환 성공");
            response.put("groupList",groupInfoList);
        }catch (Exception e){
            response.put("success",false);
            response.put("message",e.getMessage());
            response.put("groupList",null);
        }

        return ResponseEntity.ok(response);
    }
}
