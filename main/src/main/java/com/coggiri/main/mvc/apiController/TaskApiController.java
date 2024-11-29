package com.coggiri.main.mvc.apiController;

import com.coggiri.main.mvc.domain.dto.TaskRegisterDTO;
import com.coggiri.main.mvc.service.TaskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "태스크 관련 기능",description = "Task 관련 API")
@RestController
@RequestMapping("/api/task")
public class TaskApiController {

    @Autowired
    private TaskService taskService;

    @PostMapping("createTask")
    public ResponseEntity<Map<String,Object>> createTask(@RequestBody TaskRegisterDTO taskRegisterDTO){
        Map<String,Object> response = new HashMap<>();

        try {
            taskService.createTask(taskRegisterDTO);
        }catch (Exception e){

        }

        return ResponseEntity.ok(response);
    }

}
