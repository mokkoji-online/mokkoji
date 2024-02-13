package online.mokkoji.common.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventErrorCode implements ErrorCode {

    ALREADY_CLOSED_EVENT(404, "이미 닫힌 세션입니다"),
    HOST_NOT_FOUND(404, "호스트가 아닙니다."),
    FILE_EXTENSION_NOT_FOUND(400, "파일 확장자가 없습니다"),
    EVENT_NOT_FOUND(404,"행사가 없습니다");

    private final Integer errorCode;
    private final String errorMessage;
}
