package net.bgmgr.global.exception;

public class InvalidValueException extends BusinessException {

    public InvalidValueException(final ErrorCode errorCode) {
        super(errorCode);
    }
}