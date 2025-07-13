package dgu.newsee.global.exception;

import dgu.newsee.global.payload.BaseErrorCode;
import dgu.newsee.global.payload.ResponseCode;

public class AiServerException extends GeneralException {
    public AiServerException(BaseErrorCode code) {
        super(ResponseCode.AI_SERVER_DOWN);
    }
}
