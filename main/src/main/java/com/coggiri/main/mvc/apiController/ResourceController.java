package com.coggiri.main.mvc.apiController;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.ibatis.javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Tag(name = "리소스 기능",description = "리소스 관련 API")
@RestController
@RequestMapping("/api/app")
public class ResourceController {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Value("${file.upload.directory}")
    private String uploadDirectory;

//    @ResponseBody
    @GetMapping(value = "uploads/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable("fileName") String fileName){

        try{
            String filePath = uploadDirectory + "/" + fileName;
            FileSystemResource resource = new FileSystemResource(filePath);
            String userDirectoryPath = System.getProperty("user.dir");

            log.info("filePath: " + filePath);
            log.info("curPath: " + userDirectoryPath);
            if(!resource.exists()){
                log.info("file doesn't exist");
                throw new RuntimeException("요청하신 이미지가 존재하지 않습니다.");
            }
            Path result = Paths.get(filePath);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(Files.probeContentType(result)))
                    .body(resource);
        }catch (Exception e){
            throw new RuntimeException("요청하신 이미지가 존재하지 않습니다.");
        }
    }
}
