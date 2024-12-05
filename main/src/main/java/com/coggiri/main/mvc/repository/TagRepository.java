package com.coggiri.main.mvc.repository;

import com.coggiri.main.mvc.domain.entity.Tag;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface TagRepository {
    int createTag(Tag tag);
    ArrayList<Integer> getTagIds(Tag tag);
    ArrayList<String> getTagNames(ArrayList<Integer>  tagsId);
}
