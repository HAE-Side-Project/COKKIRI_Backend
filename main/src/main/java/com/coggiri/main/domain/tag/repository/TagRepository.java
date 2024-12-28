package com.coggiri.main.domain.tag.repository;

import com.coggiri.main.domain.tag.model.entity.Tag;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository {
    int addGroupTag(@Param("groupId")Long groupId,@Param("tags") Tag tag);
    int addTaskTag(@Param("taskId")Long taskId,@Param("tags") Tag tag);
    int deleteGroupTag(@Param("groupId") Long groupId,@Param("tags") Tag tag);
    int deleteTaskTag(@Param("taskId") Long taskId,@Param("tags") Tag tag);
    String[] getGroupTag(Long groupId);
    String[] getTaskTag(Long taskId);
}
