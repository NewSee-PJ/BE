package dgu.newsee.global.payload;

import dgu.newsee.global.payload.ErrorReasonDTO;

public interface BaseErrorCode {
    String getCode();
    String getMessage();
    ErrorReasonDTO getReason();
    ErrorReasonDTO getReasonHttpStatus();
}
