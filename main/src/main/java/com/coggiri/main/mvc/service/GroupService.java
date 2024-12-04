package com.coggiri.main.mvc.service;

import com.coggiri.main.customEnums.Role;
import com.coggiri.main.mvc.domain.dto.GroupInfoDTO;
import com.coggiri.main.mvc.domain.dto.GroupRegisterDTO;
import com.coggiri.main.mvc.domain.dto.SearchInFoDTO;
import com.coggiri.main.mvc.domain.entity.Group;
import com.coggiri.main.mvc.domain.entity.User;
import com.coggiri.main.mvc.domain.entity.UserGroupRole;
import com.coggiri.main.mvc.repository.GroupRepository;
import com.coggiri.main.mvc.repository.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class GroupService {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TagRepository tagRepository;

    @Value("${file.upload.directory}")
    private String uploadDirectory;

    private static final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png", "gif", "bmp", "webp"};

    public List<GroupInfoDTO> getGroupList(SearchInFoDTO searchInFoDTO){
        int offset = searchInFoDTO.getPageNum()-1;
        if(offset < 0) throw new IllegalArgumentException("잘못된 Page Number입니다.");

        Map<String,Object> params = new HashMap<>();
        params.put("keyword",searchInFoDTO.getKeyword());
        params.put("page",offset+100);
        params.put("offset",offset*100);
        List<GroupInfoDTO> groupList =  groupRepository.getGroupList(params);

        for(GroupInfoDTO group : groupList){
            if(!group.getThumbnailPath().isEmpty()){
                try {
                    String filePath = uploadDirectory + "/" +group.getThumbnailPath();
                    Path path = Paths.get(filePath);

                    if (Files.exists(path)) {
                        byte[] fileContent = Files.readAllBytes(path);
                        String mimeType = Files.probeContentType(path);
                        String base64Image = Base64.getEncoder().encodeToString(fileContent);

                        group.setThumbnailBase64("data:"+mimeType+";base64,"+base64Image);
                    }
                }catch (Exception e){
                    log.info("Thumbnail File Create Failed" + e.getMessage());
                }
            }
        }

        return groupList;
    }

    public boolean createGroup(String userId,GroupRegisterDTO groupRegisterDTO, MultipartFile image){
        User user = userService.findUserById(userId).orElseThrow(() ->
                new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        String fileName = "";
        if(!image.isEmpty()){
            String fileExtension = getFileExtension(image.getOriginalFilename());

            if(!isValidImageExtension(fileExtension)) return false;

            String filePath = "";
            while(true) {
                UUID uuid = UUID.randomUUID();
                fileName = uuid.toString() + "." + fileExtension;
                filePath = uploadDirectory + "/" + fileName;
                File newFile = new File(filePath);
                if(!newFile.exists()) break;
            }

            try {
                saveFile(image, filePath);
            } catch (Exception e) {
                log.info("createGroup Error: " + e.getMessage());
            }
        }

        Group groupInfo = new Group(groupRegisterDTO,fileName);
        // 그룹 정보 저장
        groupRepository.createGroup(groupInfo);
        // 그룹 태그 저장
//        tagRepository.addTag
        if(userService.addUserRole(new UserGroupRole(user.getId(),groupInfo.getGroupId(), Role.ADMIN.name())) == 0){
            return false;
        }

        return true;
    }

    public boolean deleteGroup(int groupId){
        GroupInfoDTO groupInfoDTO = groupRepository.getGroup(groupId);

        if(!groupInfoDTO.getThumbnailPath().isEmpty()){
            String filePath = uploadDirectory + "/" + groupInfoDTO.getThumbnailPath();

            File file = new File(filePath);
            if(!file.exists()) throw new RuntimeException("존재하지 않는 썸네일 파일입니다.");
            if(!file.delete()) throw new RuntimeException("썸네일 파일 삭제 실패");
        }

        if(groupRepository.deleteGroup(groupId) == 0) throw new RuntimeException("데이터베이스 삭제 실패");
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
