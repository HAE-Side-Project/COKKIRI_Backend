package com.coggiri.main.mvc.repository;

import com.coggiri.main.mvc.domain.dto.TaskRoleInfoDTO;
import com.coggiri.main.mvc.domain.entity.Task;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository {
    int createTask(Task task);
    int addTaskRole(TaskRoleInfoDTO taskRoleInfoDTO);
}
