package com.coggiri.main.mvc.apiController;

import com.coggiri.main.mvc.service.TagService;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tag")
public class TagApiController {
    @Autowired
    private TagService tagService;

    @PostMapping("/createTag")
    public ResponseEntity<Map<String,Object>> createTag(@RequestBody Map<String,Object> map){
        Map<String, Object> response = new HashMap<>();
        List<String> tagList = (List<String>) map.get("tag");
        String[] tags = tagList.toArray(new String[0]);

        tagService.createTag(tags);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/getTagsId")
    public ResponseEntity<Map<String,Object>> getTags(@RequestBody Map<String,Object> map){
        Map<String, Object> response = new HashMap<>();
        List<String> tagList = (List<String>) map.get("tag");
        String[] tags = tagList.toArray(new String[0]);

        ArrayList<Integer> tagIds = tagService.getTagId(tags);
        ArrayList<String> tagNames = tagService.getTagName(tagIds);
        response.put("tag",tagIds);
        response.put("name",tagNames);

        return ResponseEntity.ok(response);
    }
}
