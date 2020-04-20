package net.boardrank.global.exception;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException() {
        super("can not find a Entity", ErrorCode.ENTITY_NOT_FOUND);
    }
}