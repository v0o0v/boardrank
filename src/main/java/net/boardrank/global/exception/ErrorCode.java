package net.boardrank.global.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(400, "C002", " Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "C003", " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(400, "C006", "Access is Denied"),

    // Member
    EMAIL_DUPLICATION(400, "M001", "Email is Duplication"),
    LOGIN_INPUT_INVALID(400, "M002", "Login input is invalid"),

    //Thinq
    TOKEN_RESPONSE_FAIL(500, "T001", "Token Response Fail"),
    ThinqDevicePropertyNotFound(500, "T002", "Thinq Device Property Not Found"),
    Thinq_Device_Control_Fail(500, "T003", "Thinq Device Control Fail"),

    //Group
    DUPLICATED_GROUP(500, "G001", "중복되는 그룹입니다."),

    //Store
    DUPLICATED_DEVICE_NAME(500, "S001", "중복되는 디바이스 이름입니다.");

    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }


}