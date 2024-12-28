package com.coggiri.main.domain.task.service;

import com.coggiri.main.commons.Enums.TagType;
import com.coggiri.main.domain.tag.service.TagService;
import com.coggiri.main.domain.task.model.dto.request.TaskRegisterDTO;
import com.coggiri.main.domain.task.model.dto.request.TaskInfoDTO;
import com.coggiri.main.domain.task.model.entity.Task;
import com.coggiri.main.domain.task.repository.TaskRepository;
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
        // 태스크 태그 등록
        if(taskRegisterDTO.getTags().length > 0) {
            tagService.createTag(task.getTaskId(), taskRegisterDTO.getTags(), TagType.TASK.name());
        }

        // 태스크 관계 등록
        TaskInfoDTO taskInfoDTO = new TaskInfoDTO(task.getTaskId(),taskRegisterDTO.getGroupId(),taskRegisterDTO.getUserId());
        taskRepository.addTaskRole(taskInfoDTO);
    }

    @Transactional
    public void deleteTask(Long taskId){
        taskRepository.deleteTask(taskId);
    }
}
