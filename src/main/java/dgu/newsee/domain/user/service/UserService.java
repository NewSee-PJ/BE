package dgu.newsee.domain.user.service;

import dgu.newsee.domain.user.dto.UserDTO.UserResponse.UserTokenResponse;
import dgu.newsee.domain.user.dto.UserDTO.UserResponse.ProfileUpdateResponse;
import dgu.newsee.domain.user.dto.UserDTO.UserRequest.ProfileUpdateRequest;
import dgu.newsee.domain.user.dto.UserDTO.UserRequest.LevelRequest;
import dgu.newsee.domain.user.dto.UserDTO.UserResponse.UserAuthResponse;
import dgu.newsee.domain.user.dto.UserDTO.UserResponse.LevelResponse;
import dgu.newsee.domain.user.dto.UserDTO.UserResponse.UserInfoResponse;

public interface UserService {
    UserAuthResponse kakaoLogin(String accessToken);
    UserTokenResponse refreshAccessToken(String refreshToken);
    ProfileUpdateResponse updateProfile(String userId, ProfileUpdateRequest request);

    LevelResponse registerLevel(String userId, LevelRequest request);
    LevelResponse updateLevel(String userId, LevelRequest request);

    UserInfoResponse getUserInfo(String userId);
}
