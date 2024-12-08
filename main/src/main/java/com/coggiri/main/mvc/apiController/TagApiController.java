package com.coggiri.main.mvc.apiController;

import com.coggiri.main.customEnums.TagType;
import com.coggiri.main.mvc.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Tag(name = "태그",description = "태그 관련 API")
@RestController
@RequestMapping("/api/tag")
public class TagApiController {
    @Autowired
    private TagService tagService;

    @Operation(summary = "태그 추가",description = "태그 추가 API",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "비밀번호 변경",
                content = @Content(
                        schemaProperties = {
                                @SchemaProperty(name = "success", schema = @Schema(type = "boolean",description = "성공 여부")),
                                @SchemaProperty(name = "message", schema = @Schema(type = "string",description = "메세지",example = "태그 추가 완료" ))
                        }
                )
            )},
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = {
                        @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schemaProperties = {
                                        @SchemaProperty(name = "id", schema = @Schema(type = "string", description = "그룹/태스크 pk",example = "1")),
                                        @SchemaProperty(name = "type", schema = @Schema(type = "string", description = "그룹/태스크 구분",example = "GROUP/TASK"))
                                }
                        )
                }
        )
    )
    @PostMapping("/addGroupTag")
    public ResponseEntity<Map<String,Object>> createTag(@RequestBody Map<String,Object> map){
        Map<String, Object> response = new HashMap<>();
        int id = Integer.parseInt(map.get("id").toString());
        String type = map.get("type").toString();
        List<String> tagList = (List<String>) map.get("tag");
        String[] tags = tagList.toArray(new String[0]);

        try {
            tagService.createTag(id, tags, TagType.GROUP.name());
            response.put("success", true);
            response.put("message","태그 추가 완료");
        }catch (Exception e){
            response.put("success",false);
            response.put("message",e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "그룹 태그 삭제",description = "그룹 태그 삭제 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "비밀번호 변경",
                            content = @Content(
                                    schemaProperties = {
                                            @SchemaProperty(name = "success", schema = @Schema(type = "boolean",description = "성공 여부")),
                                            @SchemaProperty(name = "message", schema = @Schema(type = "string",description = "메세지",example = "그룹 태그 삭제 완료" ))
                                    }
                            )
                    )},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "groupId", schema = @Schema(type = "string", description = "그룹 pk",example = "1")),
                                            @SchemaProperty(name = "tag", schema = @Schema(type = "string", description = "그룹 태그",example = "[\"Study\"]"))
                                    }
                            )
                    }
            )
    )
    @PostMapping("/deleteGroupTag")
    public ResponseEntity<Map<String,Object>> deleteGroupTag(@RequestBody Map<String,Object> map){
        Map<String,Object> response = new HashMap<>();
        String groupId = map.get("groupId").toString();
        List<String> tagList = (List<String>) map.get("tag");
        String[] tag = tagList.toArray(new String[0]);

        try{
            tagService.deleteTag(Integer.parseInt(groupId),tag,TagType.GROUP.name());
            response.put("success",true);
            response.put("message","그룹 태그 삭제 완료");
        }catch (Exception e){
            response.put("success",false);
            response.put("message",e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "그룹 태그 정보 확인",description = "그룹 태그 정보 확인 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "비밀번호 변경",
                            content = @Content(
                                    schemaProperties = {
                                            @SchemaProperty(name = "success", schema = @Schema(type = "boolean",description = "성공 여부")),
                                            @SchemaProperty(name = "message", schema = @Schema(type = "string",description = "메세지",example = "태그 정보 획득 완료" ))
                                    }
                            )
                    )},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "groupId", schema = @Schema(type = "string", description = "그룹 pk",example = "1"))
                                    }
                            )
                    }
            )
    )
    @PostMapping("/getGroupTag")
    public ResponseEntity<Map<String,Object>> getTags(@RequestBody Map<String,Object> map){
        Map<String, Object> response = new HashMap<>();
        int groupId = Integer.parseInt((String) map.get("groupId"));

        try{
            String[] tag = tagService.getTags(groupId,TagType.GROUP.name());
            response.put("success",true);
            response.put("message","태그 정보 획득 완료");
            response.put("tag",tag);
        }catch (Exception e){
            response.put("success",false);
            response.put("message",e.getMessage());
            response.put("tag","");
        }
        return ResponseEntity.ok(response);
    }

}
