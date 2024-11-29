package com.coggiri.main.mvc.apiController;

import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;
import java.util.Map;

@Controller
public class ResourceController {
    @ResponseBody
    @GetMapping("/app/uploads/${fileName}")
    public ResponseEntity<Map<String,Object>> getImage(@PathVariable("fileName") String fileName){
        try{
            String filePath =
        }catch (Exception e){
            throw new RuntimeException("요청하신 이미지가 존재하지 않습니다.\n" + e.getMessage());
        }
    }
}
