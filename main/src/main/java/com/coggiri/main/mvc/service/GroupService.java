package com.coggiri.main.mvc.service;

import com.coggiri.main.customEnums.Role;
import com.coggiri.main.mvc.domain.dto.GroupInfoDTO;
import com.coggiri.main.mvc.domain.dto.GroupRegisterDTO;
import com.coggiri.main.mvc.domain.dto.SearchInFoDTO;
import com.coggiri.main.mvc.domain.entity.Group;
import com.coggiri.main.mvc.domain.entity.User;
import com.coggiri.main.mvc.domain.entity.UserGroupRole;
import com.coggiri.main.mvc.repository.GroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class GroupService {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
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

    public List<GroupInfoDTO> getGroupList(SearchInFoDTO searchInFoDTO){
        int offset = searchInFoDTO.getPageNum()-1;
        if(offset < 0) throw new IllegalArgumentException("잘못된 Page Number입니다.");

        System.out.println(offset);
        Map<String,Object> params = new HashMap<>();
        params.put("keyword",searchInFoDTO.getKeyword());
        params.put("page",offset+100);
        params.put("offset",offset*100);
        System.out.println(searchInFoDTO.getKeyword());
        System.out.println((offset+100) + " " + offset*100);
        List<GroupInfoDTO> groupList =  groupRepository.getGroupList(params);

        System.out.println("groupSize: " + groupList.size());

        return groupList;
    }

    public boolean createGroup(String userId,GroupRegisterDTO groupRegisterDTO, MultipartFile image){
        User user = userService.findUserById(userId).orElseThrow(() ->
                new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        String fileName = "";
        if(!image.isEmpty()){
            String fileExtension = getFileExtension(image.getOriginalFilename());

            if(!isValidImageExtension(fileExtension)) return false;

            String filePath = uploadDirectory + "/";
            while(true) {
                UUID uuid = UUID.randomUUID();
                fileName = uuid.toString() + "." + fileExtension;
                filePath += fileName;
                File newFile = new File(filePath);
                if(!newFile.exists()) break;
            }

            try {
                saveFile(image, fileName);
            } catch (Exception e) {
                log.info("createGroup Error: " + e.getMessage());
            }
        }

        Group groupInfo = new Group(groupRegisterDTO,fileName);
        groupRepository.createGroup(groupInfo);
        if(userService.addUserRole(new UserGroupRole(user.getId(),groupInfo.getGroupId(), Role.ADMIN.name())) == 0){
            return false;
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

    private void saveFile(MultipartFile file, String filePath) {
        File targetFile = new File(filePath);

        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            System.out.println("msg: " + e.getMessage());
            throw new RuntimeException("File not saved");
        }
    }
}
