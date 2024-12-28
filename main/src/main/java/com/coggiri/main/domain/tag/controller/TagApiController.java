package com.coggiri.main.domain.tag.controller;

import com.coggiri.main.commons.Enums.SuccessType;
import com.coggiri.main.commons.Enums.TagType;
import com.coggiri.main.commons.response.CustomResponse;
import com.coggiri.main.domain.tag.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "2102",
                    description = "태그 추가에 성공했습니다.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 201,
                                        "code": 2102,
                                        "message": "태그 추가에 성공했습니다."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "5005",
                    description = "태그 추가에 실패했습니다.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 500,
                                        "code": 5005,
                                        "message": "태그 추가에 실패했습니다."
                                    }
                                    """
                            )
                    )
            )
    })
    @PostMapping("/create")
    public ResponseEntity<CustomResponse<?>> createTag(@RequestBody Map<String,Object> map){
        Map<String, Object> response = new HashMap<>();
        Long id = Long.parseLong(map.get("id").toString());
        String type = map.get("type").toString();
        List<String> tagList = (List<String>) map.get("tag");
        String[] tags = tagList.toArray(new String[0]);
        tagService.createTag(id, tags, type);
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomResponse.success(SuccessType.SUCCESS_TAG_CREATE));
    }

    @Operation(summary = "그룹 태그 삭제",description = "그룹 태그 삭제 API",
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
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "2009",
                    description = "태그 삭제 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 200,
                                        "code": 2009,
                                        "message": "태그 삭제 성공했습니다."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "삭제 실패",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 500,
                                        "code": 5004,
                                        "message": "태그 삭제에 실패했습니다."
                                    }
                                    """
                            )
                    )
            )
    })
    @DeleteMapping("/delete")
    public ResponseEntity<CustomResponse<?>> deleteGroupTag(@RequestBody Map<String,Object> map){
        String groupId = map.get("groupId").toString();
        List<String> tagList = (List<String>) map.get("tag");
        String[] tag = tagList.toArray(new String[0]);

        tagService.deleteTag(Long.parseLong(groupId),tag,TagType.GROUP.name());
        return ResponseEntity.status(HttpStatus.OK).body(CustomResponse.success(SuccessType.SUCCESS_TAG_DELETE));
    }

    @Operation(summary = "그룹 태그 정보 확인",description = "그룹 태그 정보 확인 API",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "태그 삭제할 Group/Task의 Id",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "Long")
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "태그 조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 200,
                                        "code": 2010,
                                        "message": "태그 조회 성공했습니다.",
                                        "data": [
                                            "front",
                                            "hello2"
                                        ]
                                    }
                                    """
                            )
                    )
            )
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<CustomResponse<?>> getTags(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(CustomResponse.success(SuccessType.SUCCESS_TAG_GET,tagService.getTags(id,TagType.GROUP.name())));
    }

}
