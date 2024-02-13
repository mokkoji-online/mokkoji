package online.mokkoji.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import online.mokkoji.common.exception.errorcode.ErrorCode;

//발생시킨 예외를 처리해줄 클래스
@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException {
    private final ErrorCode errorCode;
}
