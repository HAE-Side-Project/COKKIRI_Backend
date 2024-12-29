package com.coggiri.main.domain.group.controller;

import com.coggiri.main.commons.Enums.Role;
import com.coggiri.main.commons.Enums.SuccessType;
import com.coggiri.main.commons.jwtUtils.RequireGroupRole;
import com.coggiri.main.commons.response.CustomResponse;
import com.coggiri.main.domain.group.model.dto.request.GroupInfoDTO;
import com.coggiri.main.domain.group.model.dto.request.GroupRegisterDTO;
import com.coggiri.main.domain.task.model.dto.request.SearchInFoDTO;
import com.coggiri.main.domain.group.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.*;
//import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "그룹 기능",description = "그룹 관련 API")
@RestController
@AllArgsConstructor
@RequestMapping("/api/group")
public class GroupApiController {
    private final GroupService groupService;

    @Operation(summary = "그룹생성", description = "그룹 생성 기능 API",
            parameters = {
                    @Parameter(name = "groupInfo",description = "그룹 정보",schema = @Schema(implementation = GroupRegisterDTO.class)),
                    @Parameter(name = "image",description = "이미지파일",schema = @Schema(implementation = MultipartFile.class))
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "2101",
                    description = "그룹 생성 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 201,
                                        "code": 2101,
                                        "message": "그룹 생성에 성공했습니다."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "5000",
                    description = "그룹 생성 실패",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 500,
                                        "code": 5002,
                                        "message": "그룹 생성 에러가 발생했습니다."
                                    }
                                    """
                            )
                    )
            )
    })
    @PostMapping(value = "/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomResponse<?>> createGroup(@Parameter(description = "그룹 정보") @RequestPart(value = "groupInfo",required = true) GroupRegisterDTO groupRegisterDTO,
                                                      @Parameter(description = "이미지파일") @RequestPart(value = "image",required = false) MultipartFile image,
                                                         HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);

        groupService.createGroup(groupRegisterDTO,image,token);
        return ResponseEntity.status(HttpStatus.OK).body(CustomResponse.success(SuccessType.SUCCESS_GROUP_CREATE));
    }

    @Operation(summary = "그룹 리스트 정보 리스트", description = "그룹 정보 리스트 반환 API",
            parameters = {
                    @Parameter(name = "keyword",description = "검색어",example = "Study"),
                    @Parameter(name = "pageNum",description = "페이지 개수", example = "1")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "2007",
                    description = "그룹 리스트 조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    "status": 200,
                                        "code": 2007,
                                        "message": "그룹 리스트 조회에 성공했습니다.",
                                        "data": {
                                            "pageNum": 1,
                                            "totalNum": 1,
                                            "groupInfoDTOList": [
                                                {
                                                    "groupId": 10,
                                                    "groupCategory": 0,
                                                    "groupName": "tempStudy",
                                                    "groupNumber": 1,
                                                    "thumbnailPath": "D:/Code/git/COKKIRI_Backend/main/app/uploads/f38a3aec-abb5-491a-840d-12f330edd859.png",
                                                    "groupIntro": "그룹 생성테스트입니다.",
                                                    "groupRule": "없습니다.",
                                                    "groupCondition": "가입조건은 없습니다",
                                                    "tags": null,
                                                    "thumbnailBase64": null
                                                }
                                            ]
                                        }
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "4016",
                    description = "잘못된 페이지 숫자입니다.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 400,
                                        "code": 4016,
                                        "message": "잘못된 페이지 숫자입니다."
                                    }
                                    """
                            )
                    )
            )
    })
    @ResponseBody
    @GetMapping(value = "/list")
    public ResponseEntity<CustomResponse<?>> getGroupList(@RequestParam(name = "keyword", required = false) String keyword, @RequestParam(name = "pageNum", defaultValue = "1") Long pageNum){
        SearchInFoDTO searchInFoDTO = new SearchInFoDTO(keyword,pageNum);
        return ResponseEntity.status(HttpStatus.OK).body(CustomResponse.success(SuccessType.SUCCESS_GROUP_LIST,groupService.getGroupList(searchInFoDTO)));
    }

    @Operation(summary = "그룹 삭제",description = "그룹 삭제 API",
            parameters = {
                @Parameter(
                        name = "groupId",
                        description = "삭제할 그룹의 Id",
                        required = true,
                        in = ParameterIn.PATH,
                        schema = @Schema(type = "Long")
                )
            })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "2008",
                    description = "그룹 삭제 성공했습니다",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 200,
                                        "code": 2008,
                                        "message": "그룹 삭제 성공했습니다"
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "4403",
                    description = "썸네일이 존재하지 않습니다.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 404,
                                        "code": 4403,
                                        "message": "썸네일이 존재하지 않습니다."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "5003",
                    description = "썸네일 파일 삭제 실패했습니다.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "status": 500,
                                        "code": 5003,
                                        "message": "썸네일 파일 삭제 실패했습니다."
                                    }
                                    """
                            )
                    )
            )
    })
    @RequireGroupRole(value=Role.ADMIN, groupIdParameter = "groupId")
    @ResponseBody
    @DeleteMapping("/delete/{groupId}")
    public ResponseEntity<CustomResponse<?>> deleteGroup(@PathVariable("groupId") Long groupId){
        groupService.deleteGroup(groupId);
        return ResponseEntity.status(HttpStatus.OK).body(CustomResponse.success(SuccessType.SUCCESS_GROUP_DELETE));
    }

}
