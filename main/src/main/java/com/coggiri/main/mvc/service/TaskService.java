package com.coggiri.main.mvc.service;

import com.coggiri.main.mvc.domain.dto.TaskRegisterDTO;
import com.coggiri.main.mvc.domain.dto.TaskRoleInfoDTO;
import com.coggiri.main.mvc.domain.entity.Task;
import com.coggiri.main.mvc.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Transactional
    public void createTask(TaskRegisterDTO taskRegisterDTO){
        Task task = new Task(taskRegisterDTO);
        taskRepository.createTask(task);

        TaskRoleInfoDTO taskRoleInfoDTO = new TaskRoleInfoDTO(task.getTaskId(),taskRegisterDTO.getGroupId(),taskRegisterDTO.getUserId());
        taskRepository.addTaskRole(taskRoleInfoDTO);

        // task 등록
        // tag 등록
        // user,group task 등록
    }
}
