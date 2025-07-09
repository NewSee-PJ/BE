package dgu.newsee.global.exception;

import lombok.Getter;

@Getter
public abstract class GeneralException extends RuntimeException {

    private final BaseErrorCode code;

    protected GeneralException(BaseErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }
}
