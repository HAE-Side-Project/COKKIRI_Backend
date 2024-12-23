package com.coggiri.main.domain.group.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Tag(name = "리소스 기능",description = "리소스 관련 API")
@RestController
@RequestMapping("/api/resource")
public class ResourceController {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    
    @Value("${file.upload.directory}")
    private String uploadDirectory;

    @Operation(summary = "이미지 접근",description = "이미지 반환",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "이미지 성공적으로 반환",
                            content = @Content(
                                    mediaType = "image/*",
                                    schema = @Schema(type = "string", format = "binary")
                            )
                    )
            },
            parameters = {
                    @Parameter(name = "filename",description = "파일명",example = "예시파일.jpg")
            }
    )
    @GetMapping(value = "/getImage")
    public ResponseEntity<Resource> getImage(@RequestParam(name = "filename", required = false) String fileName){

        try{
            String filePath = uploadDirectory + "/" + fileName;
            FileSystemResource resource = new FileSystemResource(filePath);

            log.info("filePath: " + filePath);

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
