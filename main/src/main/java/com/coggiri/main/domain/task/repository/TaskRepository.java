package com.coggiri.main.domain.task.repository;

import com.coggiri.main.mvc.domain.dto.TaskInfoDTO;
import com.coggiri.main.mvc.domain.entity.Task;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository {
    int createTask(Task task);
    int deleteTask(int taskId);
    int addTaskRole(TaskInfoDTO taskInfoDTO);
}
