package dgu.newsee.global.exception;

import dgu.newsee.global.payload.BaseErrorCode;

public class UserException extends GeneralException {
    public UserException(BaseErrorCode code) {
        super(code);
    }
}
