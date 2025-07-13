package dgu.newsee.global.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean isSuccess;
    private String code;
    private String message;
    private T result;

    public static <T> ApiResponse<T> success(T result) {
        return success(result, ResponseCode.COMMON_SUCCESS);
    }

    public static <T> ApiResponse<T> success(T result, ResponseCode code) {
        return ApiResponse.<T>builder()
                .isSuccess(true)
                .code(code.getCode())
                .message(code.getMessage())
                .result(result)
                .build();
    }

    public static <T> ApiResponse<T> failure(ErrorReasonDTO reason) {
        return ApiResponse.<T>builder()
                .isSuccess(false)
                .code(reason.getCode())
                .message(reason.getMessage())
                .result(null)
                .build();
    }
}
