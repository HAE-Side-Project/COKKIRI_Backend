package com.coggiri.main.mvc.service;

import com.coggiri.main.customEnums.TagType;
import com.coggiri.main.mvc.domain.entity.Tag;
import com.coggiri.main.mvc.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {
    @Autowired
    TagRepository tagRepository;

    public void createTag(int id,String[] tags,String tagType){

        Tag tag = new Tag(tags);
        if(TagType.valueOf(tagType) == TagType.GROUP){
            tagRepository.addGroupTag(id,tag);
        }else if(TagType.valueOf(tagType) == TagType.TASK){
            tagRepository.addTaskTag(id,tag);
        }
    }

    public void deleteTag(int id,String[] tags,String tagType){
        Tag tag = new Tag(tags);
        if(TagType.valueOf(tagType) == TagType.GROUP){
            tagRepository.deleteGroupTag(id,tag);
        }else if(TagType.valueOf(tagType) == TagType.TASK){
            tagRepository.deleteTaskTag(id,tag);
        }
    }

    public String[] getTags(int id, String tagType){
        String[] tags = null;
        if(TagType.valueOf(tagType) == TagType.GROUP){
            tags = tagRepository.getGroupTag(id);
        }else if(TagType.valueOf(tagType) == TagType.TASK){
            tags = tagRepository.getTaskTag(id);
        }
        return tags;
    }


}
