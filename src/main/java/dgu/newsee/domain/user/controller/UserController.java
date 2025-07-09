package dgu.newsee.domain.user.controller;

import dgu.newsee.domain.user.dto.UserDTO.UserRequest.SocialLoginRequest;
import dgu.newsee.domain.user.dto.UserDTO.UserResponse.UserAuthResponse;
import dgu.newsee.domain.user.service.UserService;
import dgu.newsee.global.payload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "Auth 컨트롤러", description = "소셜 로그인 관련 API")
public class UserController {

    private final UserService userService;


}
