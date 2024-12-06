package com.coggiri.main.mvc.service;

import com.coggiri.main.mvc.domain.dto.GroupInfoDTO;
import com.coggiri.main.mvc.domain.entity.Tag;
import com.coggiri.main.mvc.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TagService {
    @Autowired
    TagRepository tagRepository;

    public void createTag(String[] tags){
        Tag tag = new Tag(tags);
        tagRepository.createTag(tag);
    }

    public void addGroupTagRole(int groupId,String[] tags){
        Tag tag = new Tag(tags);
        ArrayList<Integer> tagIds =  tagRepository.getTagIds(tag);
        tagRepository.addGroupTagRole(groupId,tagIds);
    }

    public void deleteGroupTagRole(GroupInfoDTO groupInfoDTO){

        deleteTags(groupInfoDTO.getTags());
    }

    public String[] getGroupTags(int groupId){
        return tagRepository.getGroupTags(groupId);
    }

    public ArrayList<Integer> getTagId(String[] tags){
        Tag tag = new Tag(tags);
        return tagRepository.getTagIds(tag);
    }

    public ArrayList<String> getTagName(ArrayList<Integer> tagsId){
        return tagRepository.getTagNames(tagsId);
    }

    public void deleteTags(String[] tags){
        Tag tag = new Tag(tags);
        tagRepository.deleteTags(tag);
    }
}
