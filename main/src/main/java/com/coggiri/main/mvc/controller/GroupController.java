package com.coggiri.main.mvc.controller;

import com.coggiri.main.mvc.domain.dto.GroupInfoDTO;
import com.coggiri.main.mvc.domain.dto.GroupRegisterDTO;
import com.coggiri.main.mvc.domain.dto.SearchInFoDTO;
import com.coggiri.main.mvc.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Tag(name = "그룹 기능",description = "그룹 관련 API")
@RestController
@RequestMapping("/api/group")
public class GroupController {

    private GroupService groupService;

    @Autowired
    GroupController(GroupService groupService){
        this.groupService = groupService;
    }

    @Operation(summary = "그룹생성", description = "그룹 생성 기능 API",
        responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "그룹생성 API",
                    content = @Content(
                            schemaProperties = {
                                    @SchemaProperty(name = "success", schema = @Schema(type = "boolean",description = "성공 여부")),
                                    @SchemaProperty(name = "message",schema = @Schema(type = "string",description = "성공 및 오류메세지 반환"))
                            }
                    )
            )
        }
    )
    @PostMapping(value = "/createGroup",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String,Object>> createGroup( @RequestPart(value = "userId",required = true) String userId,
                                                           @RequestPart(value = "groupInfo",required = true) GroupRegisterDTO groupRegisterDTO,
                                                          @RequestPart(value = "image",required = false) MultipartFile image){
        Map<String,Object> response = new HashMap<>();

        if(groupService.createGroup(userId,groupRegisterDTO,image)){
            response.put("success",true);
            response.put("message","그룹 생성 완료");
        }else{
            response.put("success",false);
            response.put("message","그룹 생성 실패");
        }

        return ResponseEntity.ok(response);
    }
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
    @PostMapping("/getGroupList")
    public ResponseEntity<Map<String,Object>> getGroupList(@RequestBody SearchInFoDTO searchInFoDTO){
        Map<String,Object> response = new HashMap<>();

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
