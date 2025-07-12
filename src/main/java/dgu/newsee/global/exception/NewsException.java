package dgu.newsee.global.exception;

import dgu.newsee.global.payload.BaseErrorCode;

public class NewsException extends GeneralException {
    public NewsException(BaseErrorCode code) {
        super(code);
    }
}