package dgu.newsee.domain.user.controller;

import dgu.newsee.domain.user.dto.UserDTO.UserRequest.SocialLoginRequest;
import dgu.newsee.domain.user.dto.UserDTO.UserResponse.UserAuthResponse;
import dgu.newsee.domain.user.dto.UserDTO.UserResponse.ProfileUpdateResponse;
import dgu.newsee.domain.user.dto.UserDTO.UserRequest.ProfileUpdateRequest;
import dgu.newsee.domain.user.service.UserService;
import dgu.newsee.global.payload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
}
