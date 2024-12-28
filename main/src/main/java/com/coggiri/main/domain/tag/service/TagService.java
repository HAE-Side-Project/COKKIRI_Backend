package com.coggiri.main.domain.tag.service;

import com.coggiri.main.commons.Enums.ErrorType;
import com.coggiri.main.commons.Enums.TagType;
import com.coggiri.main.commons.exception.customException;
import com.coggiri.main.domain.tag.model.entity.Tag;
import com.coggiri.main.domain.tag.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {
    @Autowired
    TagRepository tagRepository;

    public void createTag(Long id,String[] tags,String tagType){

        Tag tag = new Tag(tags);
        try {
            if (TagType.valueOf(tagType) == TagType.GROUP) {
                tagRepository.addGroupTag(id, tag);
            } else if (TagType.valueOf(tagType) == TagType.TASK) {
                tagRepository.addTaskTag(id, tag);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new customException(ErrorType.INTERNAL_TAG_CREATE);
        }
    }

    public void deleteTag(Long id,String[] tags,String tagType){
        Tag tag = new Tag(tags);
        try {
            if (TagType.valueOf(tagType) == TagType.GROUP) {
                tagRepository.deleteGroupTag(id, tag);
            } else if (TagType.valueOf(tagType) == TagType.TASK) {
                tagRepository.deleteTaskTag(id, tag);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new customException(ErrorType.INTERNAL_TAG_DELETE);
        }
    }

    public String[] getTags(Long id, String tagType){
        String[] tags = null;
        if(TagType.valueOf(tagType) == TagType.GROUP){
            tags = tagRepository.getGroupTag(id);
        }else if(TagType.valueOf(tagType) == TagType.TASK){
            tags = tagRepository.getTaskTag(id);
        }
        return tags;
    }


}
