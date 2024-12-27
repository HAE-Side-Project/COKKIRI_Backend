package com.coggiri.main.domain.group.service;

import com.coggiri.main.commons.Enums.ErrorType;
import com.coggiri.main.commons.Enums.Role;
import com.coggiri.main.commons.Enums.TagType;
import com.coggiri.main.commons.exception.customException;
import com.coggiri.main.domain.group.model.dto.request.GroupInfoDTO;
import com.coggiri.main.domain.group.model.dto.request.GroupRegisterDTO;
import com.coggiri.main.domain.group.model.dto.response.GroupListResponse;
import com.coggiri.main.domain.task.model.dto.request.SearchInFoDTO;
import com.coggiri.main.domain.group.model.entity.Group;
import com.coggiri.main.domain.user.model.entity.User;
import com.coggiri.main.domain.user.model.entity.UserGroupRole;
import com.coggiri.main.domain.group.repository.GroupRepository;
import com.coggiri.main.domain.tag.service.TagService;
import com.coggiri.main.domain.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RequiredArgsConstructor
@Service
public class GroupService {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final GroupRepository groupRepository;
    private final UserService userService;
    private final TagService tagService;

    @Value("${file.upload.directory}")
    private String uploadDirectory;

    private static final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png", "gif", "bmp", "webp"};

    @Transactional
    public GroupListResponse getGroupList(SearchInFoDTO searchInFoDTO){
        long page = searchInFoDTO.getPageNum();
        long offset = searchInFoDTO.getOffset();
        if(page < 0) throw new IllegalArgumentException("잘못된 Page Number입니다.");

        System.out.println(offset + " " + ((page-1)*offset + 1));

        List<GroupInfoDTO> groupList =  groupRepository.getGroupList(searchInFoDTO.getKeyword(),offset,(page-1)*offset + 2);
        Long totalNum = groupRepository.countGroupTotalNum();
        return GroupListResponse.of(page,totalNum,groupList);
    }

    public GroupInfoDTO getGroup(int groupId){
        GroupInfoDTO groupInfoDTO = groupRepository.getGroup(groupId);
        String[] tags = tagService.getTags(groupId,TagType.GROUP.name());

        groupInfoDTO.setTags(tags);

        return groupInfoDTO;
    }

    public Long countGroupTotalNum(){
        return groupRepository.countGroupTotalNum();
    }

    @Transactional
    public void createGroup(GroupRegisterDTO groupRegisterDTO, MultipartFile image,String token){
        User user = userService.findUserById(token).orElseThrow(() ->
                new customException(ErrorType.NOT_FOUND_USER));

        String fileName = "";
        if(!image.isEmpty()){
            String fileExtension = getFileExtension(image.getOriginalFilename());

            if(!isValidImageExtension(fileExtension)){
                throw new customException(ErrorType.INVALID_FILE_NOT_EXTENSION);
            }

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
                throw new customException(ErrorType.INTERNAL_GROUP_CREATE);
            }
        }

        Group groupInfo = new Group(groupRegisterDTO,"/uploads/" + fileName);
        // 그룹 정보 저장
        if(groupRepository.createGroup(groupInfo) == 0){
            throw new customException(ErrorType.INTERNAL_GROUP_CREATE);
        }

        if(groupRegisterDTO.getGroupTags().length > 0) {
            tagService.createTag(groupInfo.getGroupId(),groupRegisterDTO.getGroupTags(), TagType.GROUP.name());
        }
        // 그룹 유저 롤 저장
        if (userService.addUserRole(new UserGroupRole(user.getId(), groupInfo.getGroupId(), Role.ADMIN.name())) == 0) {
            throw new customException(ErrorType.INTERNAL_GROUP_CREATE);
        }
    }

    @Transactional
    public boolean deleteGroup(int groupId){
        GroupInfoDTO groupInfoDTO = getGroup(groupId);

        if(!groupInfoDTO.getThumbnailPath().isEmpty()){
            String filePath = uploadDirectory + "/" + groupInfoDTO.getThumbnailPath();
            log.info("filePath: " + filePath);
            File file = new File(filePath);
            if(!file.exists()) throw new RuntimeException("존재하지 않는 썸네일 파일입니다.");
            if(!file.delete()) throw new RuntimeException("썸네일 파일 삭제 실패");
        }

        if(groupInfoDTO.getTags().length > 0) {
            tagService.deleteTag(groupInfoDTO.getGroupId(),groupInfoDTO.getTags(),TagType.GROUP.name());
        }

        userService.deleteUserRoleByGroupId(groupId);
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
