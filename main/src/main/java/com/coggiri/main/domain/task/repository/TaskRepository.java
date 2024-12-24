package com.coggiri.main.domain.task.repository;

import com.coggiri.main.domain.task.model.dto.request.TaskInfoDTO;
import com.coggiri.main.domain.task.model.entity.Task;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository {
    int createTask(Task task);
    int deleteTask(int taskId);
    int addTaskRole(TaskInfoDTO taskInfoDTO);
}
