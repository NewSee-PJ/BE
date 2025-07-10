package dgu.newsee.domain.user.service;

import dgu.newsee.domain.user.dto.UserDTO.UserResponse.ProfileUpdateResponse;
import dgu.newsee.domain.user.dto.UserDTO.UserRequest.ProfileUpdateRequest;
import dgu.newsee.domain.user.dto.UserDTO.UserRequest.LevelRequest;
import dgu.newsee.domain.user.dto.UserDTO.UserResponse.UserAuthResponse;
import dgu.newsee.domain.user.dto.UserDTO.UserResponse.LevelResponse;

public interface UserService {
    UserAuthResponse kakaoLogin(String accessToken);
    ProfileUpdateResponse updateProfile(String userId, ProfileUpdateRequest request);

    LevelResponse registerLevel(String userId, LevelRequest request);
    LevelResponse updateLevel(String userId, LevelRequest request);

}
