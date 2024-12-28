package com.coggiri.main.domain.task.controller;

import com.coggiri.main.commons.Enums.SuccessType;
import com.coggiri.main.commons.response.CustomResponse;
import com.coggiri.main.domain.task.model.dto.request.TaskRegisterDTO;
import com.coggiri.main.domain.tag.service.TagService;
import com.coggiri.main.domain.task.service.TaskService;
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
import java.util.Map;

@Tag(name = "태스크 관련 기능",description = "Task 관련 API (아직 구현안됌 테스트 XXXX")
@RestController
@RequestMapping("/api/task")
public class TaskApiController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TagService tagService;

    @Operation(summary = "태스크 생성",description = "태스크 생성 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "2103",
                    description = "태스크 추가 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 201,
                                        "code": 2103,
                                        "message": "태스크 추가에 성공했습니다."
                                    }
                                    """
                            )
                    )
            )
    })
    @PostMapping("create")
    public ResponseEntity<CustomResponse<?>> createTask(@RequestBody TaskRegisterDTO taskRegisterDTO){
        taskService.createTask(taskRegisterDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomResponse.success(SuccessType.SUCCESS_TASK_CREATE));
    }

    @Operation(summary = "태스크 삭제", description = "태스크 삭제 API",
            parameters = {
                    @Parameter(
                            name = "taskId",
                            description = "삭제할 Task Id",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "Long")
                    )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "2011",
                    description = "태스크 삭제 성공했습니다.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 200,
                                        "code": 2011,
                                        "message": "태스크 삭제 성공했습니다."
                                    }
                                    """
                            )
                    )
            )
    })
    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<CustomResponse<?>> deleteTask(@PathVariable("taskId") Long taskId){
        taskService.deleteTask(taskId);
        return ResponseEntity.status(HttpStatus.OK).body(CustomResponse.success(SuccessType.SUCCESS_TASK_DELETE));
    }
    
}
