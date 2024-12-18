package com.coggiri.main.mvc.service;

import com.coggiri.main.customEnums.TagType;
import com.coggiri.main.mvc.domain.dto.TaskRegisterDTO;
import com.coggiri.main.mvc.domain.dto.TaskInfoDTO;
import com.coggiri.main.mvc.domain.entity.Task;
import com.coggiri.main.mvc.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TagService tagService;

    @Transactional
    public void createTask(TaskRegisterDTO taskRegisterDTO){
        Task task = new Task(taskRegisterDTO);

        // 태스크 등록
        taskRepository.createTask(task);
        log.info("task 등록");
        // 태스크 태그 등록
        if(taskRegisterDTO.getTags().length > 0) {
            tagService.createTag(task.getTaskId(), taskRegisterDTO.getTags(), TagType.TASK.name());
            log.info("task 태그 등록");
        }

        // 태스크 관계 등록
        TaskInfoDTO taskInfoDTO = new TaskInfoDTO(task.getTaskId(),taskRegisterDTO.getGroupId(),taskRegisterDTO.getUserId());
        taskRepository.addTaskRole(taskInfoDTO);
        log.info("task 관계 등록");

    }

    @Transactional
    public void deleteTask(int taskId){
        taskRepository.deleteTask(taskId);
    }
}
