package dgu.newsee.global.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode implements BaseErrorCode {
    // ✅ 공통
    COMMON_SUCCESS(ResponseCodeType.SUCCESS, "COMMON200", "성공입니다.", HttpStatus.OK),
    COMMON_FAIL(ResponseCodeType.ERROR, "COMMON500", "서버 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // ✅ 인증 관련
    USER_UNAUTHORIZED(ResponseCodeType.ERROR, "AUTH401", "인증되지 않은 사용자입니다.", HttpStatus.UNAUTHORIZED),
    USER_FORBIDDEN(ResponseCodeType.ERROR, "AUTH403", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    USER_NOT_FOUND(ResponseCodeType.ERROR, "AUTH404", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(ResponseCodeType.ERROR, "AUTH409", "이미 존재하는 사용자입니다.", HttpStatus.BAD_REQUEST),
    INVALID_ACCESS_TOKEN(ResponseCodeType.ERROR, "AUTH410", "유효하지 않은 액세스 토큰입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN(ResponseCodeType.ERROR, "AUTH411", "유효하지 않은 리프레시 토큰입니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(ResponseCodeType.ERROR, "AUTH412", "토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),

    // ✅ 뉴스 관련
    NEWS_NOT_FOUND(ResponseCodeType.ERROR, "NEWS404", "해당 뉴스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NEWS_ALREADY_SAVED(ResponseCodeType.ERROR, "NEWS409", "이미 저장된 뉴스입니다.", HttpStatus.BAD_REQUEST),
    NEWS_SAVE_SUCCESS(ResponseCodeType.SUCCESS, "NEWS201", "뉴스 저장에 성공했습니다.", HttpStatus.OK),
    NEWS_DELETE_SUCCESS(ResponseCodeType.SUCCESS, "NEWS202", "뉴스 삭제에 성공했습니다.", HttpStatus.OK),
    NEWS_SEARCH_SUCCESS(ResponseCodeType.SUCCESS, "NEWS203", "뉴스 검색 성공", HttpStatus.OK),

    // ✅ 단어장 관련
    WORD_SAVE_SUCCESS(ResponseCodeType.SUCCESS, "WORD201", "단어 저장에 성공했습니다.", HttpStatus.OK),
    WORD_ALREADY_SAVED(ResponseCodeType.ERROR, "WORD409", "이미 저장된 단어입니다.", HttpStatus.BAD_REQUEST),
    WORD_DELETE_SUCCESS(ResponseCodeType.SUCCESS, "WORD202", "단어 삭제에 성공했습니다.", HttpStatus.OK),
    WORD_NOT_FOUND(ResponseCodeType.ERROR, "WORD404", "단어를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // ✅ 설정 관련
    SETTING_RETRIEVE_SUCCESS(ResponseCodeType.SUCCESS, "SETTING200", "설정 정보를 불러왔습니다.", HttpStatus.OK),
    SETTING_UPDATE_SUCCESS(ResponseCodeType.SUCCESS, "SETTING201", "설정 변경에 성공했습니다.", HttpStatus.OK),

    // ✅ 문해력 레벨
    LEVEL_UPDATE_SUCCESS(ResponseCodeType.SUCCESS, "LEVEL200", "문해력 레벨이 변경되었습니다.", HttpStatus.OK),
    INVALID_LEVEL(ResponseCodeType.ERROR, "LEVEL400", "유효하지 않은 문해력 레벨입니다.", HttpStatus.BAD_REQUEST),

    // ✅ 기타
    INVALID_REQUEST(ResponseCodeType.ERROR, "REQ400", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER(ResponseCodeType.ERROR, "REQ401", "필수 파라미터가 누락되었습니다.", HttpStatus.BAD_REQUEST),
    PARSE_ERROR(ResponseCodeType.ERROR, "REQ402", "데이터 파싱 오류입니다.", HttpStatus.BAD_REQUEST);

    private final ResponseCodeType type;
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    // ErrorReasonDTO 객체 미리 정의
    private final ErrorReasonDTO reason;

    // 생성자에서 ErrorReasonDTO를 초기화
    ResponseCode(ResponseCodeType type, String code, String message, HttpStatus httpStatus) {
        this.type = type;
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;

        // ErrorReasonDTO 초기화
        this.reason = new ErrorReasonDTO(httpStatus, type == ResponseCodeType.SUCCESS, code, message);
    }

    @Override
    public ErrorReasonDTO getReason() {
        return this.reason;
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return this.reason;
    }
}
