package com.coggiri.main.domain.group.controller;

import com.coggiri.main.customEnums.Role;
import com.coggiri.main.jwtUtils.RequireGroupRole;
import com.coggiri.main.mvc.domain.dto.GroupInfoDTO;
import com.coggiri.main.mvc.domain.dto.GroupRegisterDTO;
import com.coggiri.main.mvc.domain.dto.SearchInFoDTO;
import com.coggiri.main.domain.group.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
//import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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

@Tag(name = "그룹 기능",description = "그룹 관련 API")
@RestController
@RequestMapping("/api/group")
public class GroupApiController {

    @Autowired
    private GroupService groupService;

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
            },
            parameters = {
                    @Parameter(name = "userId",description = "아이디",example = "asdf12344"),
                    @Parameter(name = "groupInfo",description = "그룹 정보",schema = @Schema(implementation = GroupRegisterDTO.class)),
                    @Parameter(name = "image",description = "이미지파일",schema = @Schema(implementation = MultipartFile.class))
            }
    )
    @RequestBody(content = @Content(encoding = @Encoding(name = "groupInfo",contentType = MediaType.APPLICATION_JSON_VALUE)))
    @PostMapping(value = "/createGroup",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String,Object>> createGroup( @RequestPart(value = "userId",required = true) String userId,
                                                           @RequestPart(value = "groupInfo",required = true) GroupRegisterDTO groupRegisterDTO,
                                                          @RequestPart(value = "image",required = false) MultipartFile image){
        Map<String,Object> response = new HashMap<>();
        try{
            groupService.createGroup(userId,groupRegisterDTO,image);
            response.put("success",true);
            response.put("message","그룹 생성 완료");
        }catch (Exception e){
            response.put("success",false);
            response.put("message",e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
    //수정 필요
    @Operation(summary = "그룹 리스트 정보 리스트", description = "그룹 정보 리스트 반환 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "그룹 리스트 반환 API",
                            content = @Content(
                                    schemaProperties = {
                                            @SchemaProperty(name = "success",schema = @Schema(type = "boolean",description = "성공 여부")),
                                            @SchemaProperty(name = "message",schema = @Schema(type = "string",description = "성공 및 오류메세지 반환")),
                                            @SchemaProperty(name = "groupList",schema = @Schema(implementation = GroupInfoDTO.class,description = "그룹 정보 리스트")),
                                            @SchemaProperty(name = "totalNum",schema = @Schema(type = "int",description = "그룹 전체 개수"))
                                    }
                            )
                    )
            },
            parameters = {
                    @Parameter(name = "keyword",description = "검색어",example = "Study"),
                    @Parameter(name = "pageNum",description = "페이지 개수", example = "1"),
                    @Parameter(name = "offset",description = "가져올 데이터 개수", example = "20")
            }
    )
    @ResponseBody
    @GetMapping(value = "/getGroupList")
    public ResponseEntity<Map<String,Object>> getGroupList(@RequestParam(name = "keyword", required = false) String keyword, @RequestParam(name = "pageNum", defaultValue = "1")  int pageNum, @RequestParam(name = "offset",defaultValue = "0") int offset){
        Map<String,Object> response = new HashMap<>();
        SearchInFoDTO searchInFoDTO = new SearchInFoDTO(keyword,pageNum,offset);

        try {
            List<GroupInfoDTO> groupInfoList = groupService.getGroupList(searchInFoDTO);
            int totalNum = groupService.countGroupTotalNum();
            response.put("success",true);
            response.put("message","그룹 리스트 반환 성공");
            response.put("groupList",groupInfoList);
            response.put("totalNum",totalNum);
        }catch (Exception e){
            response.put("success",false);
            response.put("message",e.getMessage());
            response.put("groupList",null);
        }

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "그룹 삭제",description = "그룹 삭제 API",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "그룹 삭제 API",
                        content = @Content(
                                schemaProperties = {
                                        @SchemaProperty(name = "success",schema = @Schema(type = "boolean",description = "성공 여부")),
                                        @SchemaProperty(name = "message",schema = @Schema(type = "string",description = "성공 및 오류메세지 반환",example = "그룹 삭제 완료")),
                                }
                        )
                )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "groupId", schema = @Schema(type = "string", description = "그룹 pk",example = "1"))
                                    }
                            )
                    }
            ))
    @RequireGroupRole(value=Role.ADMIN, groupIdParameter = "groupId")
    @ResponseBody
    @PostMapping("/deleteGroup")
    public ResponseEntity<Map<String,Object>> deleteGroup(@org.springframework.web.bind.annotation.RequestBody Map<String,Object> map){
        Map<String, Object> response = new HashMap<>();
        String groupId = map.get("groupId").toString();
        try{
            if(groupService.deleteGroup(Integer.parseInt(groupId))){
                response.put("success",true);
                response.put("message","그룹 삭제 완료");
            }
        }catch (Exception e){
            response.put("success",false);
            response.put("message",e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

}
