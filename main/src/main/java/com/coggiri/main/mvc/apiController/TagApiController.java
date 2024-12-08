package com.coggiri.main.mvc.apiController;

import com.coggiri.main.customEnums.TagType;
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

    @PostMapping("/addGroupTag")
    public ResponseEntity<Map<String,Object>> createTag(@RequestBody Map<String,Object> map){
        Map<String, Object> response = new HashMap<>();
        int id = Integer.parseInt(map.get("id").toString());
        String type = map.get("type").toString();
        List<String> tagList = (List<String>) map.get("tag");
        String[] tags = tagList.toArray(new String[0]);

        tagService.createTag(id,tags, TagType.GROUP.name());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/getGroupTag")
    public ResponseEntity<Map<String,Object>> getTags(@RequestBody Map<String,Object> map){
        Map<String, Object> response = new HashMap<>();
        int groupId = Integer.parseInt((String) map.get("groupId"));

        String[] tag = tagService.getTags(groupId,TagType.GROUP.name());
        response.put("tag",tag);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/deleteGroupTag")
    public ResponseEntity<Map<String,Object>> deleteGroupTag(@RequestBody Map<String,Object> map){
        Map<String,Object> response = new HashMap<>();
        String groupId = map.get("groupId").toString();
        List<String> tagList = (List<String>) map.get("tag");
        String[] tag = tagList.toArray(new String[0]);

        tagService.deleteTag(Integer.parseInt(groupId),tag,TagType.GROUP.name());

        response.put("success",true);
        return ResponseEntity.ok(response);
    }


}
