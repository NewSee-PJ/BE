package dgu.newsee.domain.user.controller;

import dgu.newsee.domain.user.dto.UserDTO.UserRequest.SocialLoginRequest;
import dgu.newsee.domain.user.dto.UserDTO.UserRequest.LevelRequest;
import dgu.newsee.domain.user.dto.UserDTO.UserResponse.UserAuthResponse;
import dgu.newsee.domain.user.dto.UserDTO.UserResponse.UserInfoResponse;
import dgu.newsee.domain.user.dto.UserDTO.UserResponse.LevelResponse;
import dgu.newsee.domain.user.dto.UserDTO.UserResponse.ProfileUpdateResponse;
import dgu.newsee.domain.user.dto.UserDTO.UserRequest.ProfileUpdateRequest;
import dgu.newsee.domain.user.service.UserService;
import dgu.newsee.global.exception.UserException;
import dgu.newsee.global.payload.ApiResponse;
import dgu.newsee.global.payload.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "User 컨트롤러", description = "User 관련 API")
public class UserController {

    private final UserService userService;

    @PostMapping("/kakao")
    @Operation(summary = "카카오 로그인 API", description = "카카오 access token 기반 로그인 API")
    public ApiResponse<UserAuthResponse> kakaoLogin(@RequestBody SocialLoginRequest request) {
        return ApiResponse.success(
                userService.kakaoLogin(request.getAccessToken()));
    }

    @PatchMapping("/profile")
    @Operation(summary = "프로필 수정 API", description = "사용자의 이름, 프로필 이미지, 문해력 수준을 수정합니다.")
    public ApiResponse<ProfileUpdateResponse> updateProfile(Authentication authentication, @RequestBody ProfileUpdateRequest request) {
        return ApiResponse.success(userService.updateProfile(authentication.getName(), request));
    }

    @PostMapping("/level")
    @Operation(summary = "문해력 레벨 등록 API (모두 가능)", description = "비로그인 유저는 MEDIUM으로 설정됩니다.")
    public ApiResponse<LevelResponse> registerLevel(
            Authentication authentication,
            @RequestBody LevelRequest request
    ) {
        String userId = (authentication != null) ? authentication.getName() : null;
        return ApiResponse.success(userService.registerLevel(userId, request));
    }

    @PatchMapping("/level")
    @Operation(summary = "문해력 레벨 수정 API (로그인 필요)", description = "로그인한 유저만 문해력 레벨을 수정할 수 있습니다.")
    public ApiResponse<LevelResponse> updateLevel(
            Authentication authentication,
            @RequestBody LevelRequest request
    ) {
        if (authentication == null) {
            throw new UserException(ResponseCode.USER_UNAUTHORIZED);
        }
        String userId = authentication.getName();
        return ApiResponse.success(userService.updateLevel(userId, request));
    }

    @GetMapping("/profile")
    @Operation(summary = "프로필 정보 및 가입일 조회 API", description = "로그인한 사용자의 이름, 프로필 이미지, 레벨, 가입일을 조회합니다.")
    public ApiResponse<UserInfoResponse> getProfileInfo(Authentication authentication) {
        if (authentication == null) {
            throw new UserException(ResponseCode.USER_UNAUTHORIZED);
        }
        String userId = authentication.getName();
        return ApiResponse.success(userService.getUserInfo(userId));
    }
}
