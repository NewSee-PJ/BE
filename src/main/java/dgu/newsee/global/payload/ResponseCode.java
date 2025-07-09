package dgu.newsee.global.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    COMMON_SUCCESS(ResponseCodeType.SUCCESS,"COMMON200", "성공입니다."),
    COMMON_FAIL(ResponseCodeType.ERROR,"COMMON500", "서버 오류입니다."),
    NEWS_NOT_FOUND(ResponseCodeType.ERROR,"NEWS404", "해당 뉴스를 찾을 수 없습니다."),
    USER_UNAUTHORIZED(ResponseCodeType.ERROR,"AUTH401", "인증되지 않은 사용자입니다.");


    private final ResponseCodeType type;
    private final String code;
    private final String message;
}
