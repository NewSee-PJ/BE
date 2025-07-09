package dgu.newsee.global.exception;

import dgu.newsee.global.payload.ApiResponse;
import dgu.newsee.global.payload.ErrorReasonDTO;
import dgu.newsee.global.payload.ResponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // GeneralException
    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ApiResponse<?>> handleGeneralException(GeneralException ex) {
        ErrorReasonDTO error = ex.getErrorReason();
        return ResponseEntity
                .status(error.getHttpStatus())
                .body(ApiResponse.failure(error));
    }

    // 그 외 모든 예외 (예: NullPointerException 등)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleOtherExceptions(Exception ex) {
        ErrorReasonDTO error = ResponseCode.COMMON_FAIL.getReasonHttpStatus(); // 내부 서버 오류로 처리
        return ResponseEntity
                .status(error.getHttpStatus())
                .body(ApiResponse.failure(error));
    }
}
