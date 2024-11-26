package com.coggiri.main.mvc.service;

import com.coggiri.main.mvc.domain.dto.GroupRegisterDTO;
import com.coggiri.main.mvc.domain.entity.User;
import com.coggiri.main.mvc.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserService userService;
    @Value("${file.upload.directory}")
    private String uploadDirectory;

    private static final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png", "gif", "bmp", "webp"};

    @Autowired
    GroupService(GroupRepository groupRepository,UserService userService){
        this.groupRepository = groupRepository;
        this.userService = userService;
    }

    public boolean createGroup(String userId,GroupRegisterDTO groupRegisterDTO, MultipartFile image){
        User user = userService.findUserById(userId).orElseThrow(() ->
                new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        if(!image.isEmpty()){
            String fileExtension = getFileExtension(image.getOriginalFilename());
            String fileName = groupRegisterDTO.getGroupName();
        }
        return true;
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex + 1).toLowerCase();
    }

    private boolean isValidImageExtension(String extension) {
        for (String allowedExt : ALLOWED_EXTENSIONS) {
            if (allowedExt.equals(extension)) {
                return true;
            }
        }
        return false;
    }
}
