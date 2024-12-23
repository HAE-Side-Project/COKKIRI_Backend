package com.coggiri.main.domain.tag.repository;

import com.coggiri.main.mvc.domain.entity.Tag;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository {
    int addGroupTag(@Param("groupId")int groupId,@Param("tags") Tag tag);
    int addTaskTag(@Param("taskId")int taskId,@Param("tags") Tag tag);
    int deleteGroupTag(@Param("groupId") int groupId,@Param("tags") Tag tag);
    int deleteTaskTag(@Param("taskId") int taskId,@Param("tags") Tag tag);
    String[] getGroupTag(int groupId);
    String[] getTaskTag(int taskId);
}
