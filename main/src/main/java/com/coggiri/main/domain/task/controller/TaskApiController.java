package com.coggiri.main.domain.task.controller;

import com.coggiri.main.domain.task.model.dto.TaskRegisterDTO;
import com.coggiri.main.domain.tag.service.TagService;
import com.coggiri.main.domain.task.service.TaskService;
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

import java.util.HashMap;
import java.util.Map;

@Tag(name = "태스크 관련 기능",description = "Task 관련 API (아직 구현안됌 테스트 XXXX")
@RestController
@RequestMapping("/api/task")
public class TaskApiController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TagService tagService;

    @Operation(summary = "태스크 생성",description = "태스크 생성 API",
        responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "태스크 생성",
                    content = @Content(
                            schemaProperties = {
                                    @SchemaProperty(name = "success", schema = @Schema(type = "boolean",description = "성공 여부")),
                                    @SchemaProperty(name = "message", schema = @Schema(type = "string",description = "메세지",example = "비밀번호 변경 완료" ))
                            }
                    )
            )}
    )
    @PostMapping("createTask")
    public ResponseEntity<Map<String,Object>> createTask(@RequestBody TaskRegisterDTO taskRegisterDTO){
        Map<String,Object> response = new HashMap<>();

        try {
            taskService.createTask(taskRegisterDTO);
            response.put("success",true);
            response.put("message","task 등룍 완료");
        }catch (Exception e){
            response.put("success",true);
            response.put("message",e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "태스크 삭제", description = "태스크 삭제 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "태스크 삭제",
                            content = @Content(
                                    schemaProperties = {
                                            @SchemaProperty(name = "success", schema = @Schema(type = "boolean",description = "성공 여부")),
                                            @SchemaProperty(name = "message", schema = @Schema(type = "string",description = "메세지",example = "태스크 삭제 완료" ))
                                    }
                            )
                    )},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "taskId", schema = @Schema(type = "string", description = "task pk",example = "1"))
                            })
                    }
            )
    )
    @PostMapping("deleteTask")
    public ResponseEntity<Map<String,Object>> deleteTask(@RequestBody Map<String,Object> map){
        Map<String,Object> response = new HashMap<>();
        String taskId = map.get("taskId").toString();
        try{
            taskService.deleteTask(Integer.parseInt(taskId));
            response.put("success",true);
            response.put("message","태스크 삭제 완료");
        }catch (Exception e){
            response.put("success",false);
            response.put("message",e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
    
}
