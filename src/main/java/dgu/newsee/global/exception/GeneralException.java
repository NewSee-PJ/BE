package dgu.newsee.global.exception;

import dgu.newsee.global.payload.BaseErrorCode;
import dgu.newsee.global.payload.ErrorReasonDTO;
import lombok.Getter;

@Getter
public abstract class GeneralException extends RuntimeException {

    private final BaseErrorCode code;

    protected GeneralException(BaseErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }
}
