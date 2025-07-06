package dgu.newsee.global;

import dgu.newsee.global.payload.ApiResponse;
import dgu.newsee.global.payload.ResponseCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public ApiResponse<String> healthCheck() {
        return ApiResponse.success("서버 정상 작동 중입니다.", ResponseCode.COMMON_SUCCESS);
    }
}