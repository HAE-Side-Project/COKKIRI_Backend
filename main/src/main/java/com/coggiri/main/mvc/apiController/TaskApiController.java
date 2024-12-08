package com.coggiri.main.mvc.apiController;

import com.coggiri.main.mvc.domain.dto.TaskRegisterDTO;
import com.coggiri.main.mvc.service.TaskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
