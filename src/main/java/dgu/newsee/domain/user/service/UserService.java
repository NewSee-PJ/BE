package dgu.newsee.domain.user.service;

import dgu.newsee.domain.user.dto.UserDTO.UserResponse.UserAuthResponse;

public interface UserService {
    UserAuthResponse kakaoLogin(String accessToken);
}
