package com.coggiri.main.mvc.service;

import com.coggiri.main.mvc.domain.dto.TaskRegisterDTO;
import com.coggiri.main.mvc.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public void createTask(TaskRegisterDTO taskRegisterDTO){
        // task 등록
        // tag 등록
        // user,group task 등록
    }
}
