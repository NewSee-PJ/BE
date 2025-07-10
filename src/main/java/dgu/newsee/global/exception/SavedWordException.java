package dgu.newsee.global.exception;

import dgu.newsee.global.payload.BaseErrorCode;

public class SavedWordException extends GeneralException {
    public SavedWordException(BaseErrorCode code) {
        super(code);
    }
}