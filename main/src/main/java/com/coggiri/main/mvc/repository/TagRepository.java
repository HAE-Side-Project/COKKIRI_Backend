package com.coggiri.main.mvc.repository;

import com.coggiri.main.mvc.domain.dto.GroupTagDTO;
import com.coggiri.main.mvc.domain.entity.Tag;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface TagRepository {
    int createTag(Tag tag);
    int addGroupTagRole(@Param("groupId")int groupId,@Param("tagsId") ArrayList<Integer> tagsId);
    int deleteTags(Tag tag);
    String[] getGroupTags(int groupId);
    ArrayList<Integer> getTagIds(Tag tag);
    ArrayList<String> getTagNames(ArrayList<Integer> tagsId);
}
